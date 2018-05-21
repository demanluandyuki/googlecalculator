package com.joyfulmath.googlecalculator

import java.util.*
import java.util.regex.Pattern


/**
 * Created by deman.lu on 2018/5/21 0021.
 */
class Calculate {
    var s1: String = ""
    var str: StringBuilder = StringBuilder()

    constructor(m: String){
        this.s1 = m
        try {
            eval()
        } catch (e: Exception) {
            str!!.delete(0, str!!.length)
            str!!.append("ERROR")
        }

    }

    /**
     *中缀表达式转后缀表达式
     *
     *遍历中缀的list
     *1、数字时，加入后缀list
     *2、“(”时，压栈
     *3、 若为 ')'，则依次弹栈,把弹出的运算符加入后缀表达式中，直到出现'('；
     *4、若为运算符，对做如下处置
     *   1、如果栈为空，则压栈
     *   2、如果栈不为空:
     *     1、stack.peek().equals("(")  则压栈
     *     2、比较str和stack.peek()的优先级
     *        1、如果>,则运算符压栈
     *        2、<=的情况：当栈不为空时:
     *           1、stack.peek()是左括号，压栈
     *           2、<=,把peek加入后缀表达式，弹栈
     *           3、>，把运算符压栈，停止对栈的操作
     *    执行完栈的操作之后，还得判断:如果栈为空,运算符压栈
     */

    @Throws(EmptyStackException::class)
    fun midToAfter(midList: List<String>): List<String> {
        val afterList = ArrayList<String>()
        val stack = Stack<String>()
        for (str in midList) {
            val flag = this.matchWitch(str)
            when (flag) {
                7 -> afterList.add(str)
                1 -> stack.push(str)
                2 -> {
                    var pop = stack.pop()
                    while (pop != "(") {
                        afterList.add(pop)
                        pop = stack.pop()
                    }
                }
                else -> if (stack.isEmpty()) {
                    stack.push(str)
//                    break
                } else {
                    if (stack.peek().equals("(")) {
                        stack.push(str)
//                        break
                    } else {
                        val ji1 = this.youxianji(str)
                        val ji2 = this.youxianji(stack.peek())
                        if (ji1 > ji2) {
                            stack.push(str)
                        } else {
                            while (!stack.isEmpty()) {
                                val f = stack.peek()
                                if (f == "(") {
                                    stack.push(str)
                                    break
                                } else {
                                    if (this.youxianji(str) <= this.youxianji(f)) {
                                        afterList.add(f)
                                        stack.pop()
                                    } else {
                                        stack.push(str)
                                        break
                                    }
                                }
                            }
                            if (stack.isEmpty()) {
                                stack.push(str)
                            }
                        }
//                        break
                    }
                }
            }
        }
        while (!stack.isEmpty()) {
            afterList.add(stack.pop())
        }
        val sb = StringBuffer()
        for (s in afterList) {
            sb.append("$s ")
        }
        //System.out.println(sb.toString());
        return afterList
    }

    fun youxianji(str: String): Int {
        var result = 0
        if (str == "+" || str == "-") {
            result = 1
        } else {
            result = 2
        }
        return result
    }

    fun matchWitch(s: String): Int {
        return if (s == "(") {
            1
        } else if (s == ")") {
            2
        } else if (s == "+") {
            3
        } else if (s == "-") {
            4
        } else if (s == "*") {
            5
        } else if (s == "/") {
            6
        } else {
            7
        }
    }

    fun singleEval(pop2: Double?, pop1: Double?, str: String): Double? {
        var value: Double? = 0.0
        if (str == "+") {
            value = pop2!! + pop1!!
        } else if (str == "-") {
            value = pop2!! - pop1!!
        } else if (str == "*") {
            value = pop2!! * pop1!!
        } else {
            value = pop2!! / pop1!!
        }
        return value
    }

    private var result: Double = 0.toDouble()

    fun getResult(): Double {
        return result
    }

    fun setResult(result: Double) {
        this.result = result
    }

    private var state: Int = 0

    fun getState(): Int {
        return state
    }

    fun setState(state: Int) {
        this.state = state
    }

    fun countHouzhui(list: List<String>) {
        str = StringBuilder("")
        state = 0
        result = 0.0
        val stack = Stack<Double>()
        for (str in list) {
            val flag = this.matchWitch(str)
            when (flag) {
                3, 4, 5, 6 -> {
                    val pop1 = stack.pop()
                    val pop2 = stack.pop()
                    val value = this.singleEval(pop2, pop1, str)
                    stack.push(value)
                }
                else -> {
                    val push = java.lang.Double.parseDouble(str)
                    stack.push(push)
                }
            }
        }
        if (stack.isEmpty()) {
            state = 1
        } else {
            result = stack.peek()
            str.append(stack.pop())
        }


    }

    @Throws(Exception::class)
    fun eval() {
        val list = ArrayList<String>()
        //匹配运算符、括号、整数、小数，注意-和*要加\\
        val p = Pattern.compile("[+\\-/\\*()]|\\d+\\.?\\d*")
        val m = p.matcher(s1)
        while (m.find()) {
            list.add(m.group())
        }
        val afterList = this.midToAfter(list)
        this.countHouzhui(afterList)
    }

}