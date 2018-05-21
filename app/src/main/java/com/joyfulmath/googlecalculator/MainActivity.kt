package com.joyfulmath.googlecalculator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.joyfulmath.googlecalculator.utils.TraceLog
import kotlinx.android.synthetic.main.activity_main.*


/**
 * Created by deman.lu on 2018/1/1 0001.
 *
 */
class MainActivity : AppCompatActivity() {
    //private int[] idNum = {R.id.txt0, R.id.txt1, R.id.txt2, R.id.txt3,
    //R.id.txt4, R.id.txt5, R.id.txt6, R.id.txt7, R.id.txt8, R.id.txt9};  //数字Number输入
    private val idNum = intArrayOf(R.id.txt0, R.id.txt1, R.id.txt2,
            R.id.txt3, R.id.txt4, R.id.txt5,
            R.id.txt6, R.id.txt7, R.id.txt8, R.id.txt9)
    private val idCal = intArrayOf(R.id.txtPlus, R.id.txtMinus, R.id.txtMul,
            R.id.txtDiv, R.id.txtLeft, R.id.txtRight, R.id.txtDot)
    private val buttonsCal = mutableListOf<Button>()
    private val buttonsNum = mutableListOf<Button>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        TraceLog.i()
        input.setText("")
        input.isEnabled = false
        output.text = ""

        txtIs.setOnClickListener { output.setText(Calculate(input.getText().toString()).str) }

        txtClear.setOnClickListener {
            input.setText("")
            output.text = ""
        }

        txtDel.setOnClickListener {
            if (!input.getText().toString().isEmpty()) {
                var str = input.getText().toString()
                str = input.getText().toString().substring(0, str.length - 1)
                input.setText(str)
            }
        }

//        idCal.forEachIndexed { index, it ->
//            buttonsCal[index] = findViewById<Button>(it)
//            buttonsCal[index]?.setOnClickListener{ CalOnClick(buttonsCal[index]?.text.toString())}
//        }
//
//        idNum.forEachIndexed { index, it ->
//            buttonsNum[index] = findViewById<Button>(it)
//            buttonsNum[index]?.setOnClickListener{ NumberOnClick(buttonsNum[index]?.text.toString())}
//        }

        initCalViews()
        initNumViews()
    }

    private fun initNumViews() {

        idNum.forEachIndexed{index,value->
            var button:Button? = findViewById<Button>(value)
            button?.setOnClickListener(
                    NumberOnClick(button.text.toString())
                    )
        }


    }

    private fun initCalViews() {

        idCal.forEach {
            var button:Button?  = findViewById<Button>(it)
            TraceLog.i("${button?.toString()}")
            button?.setOnClickListener(
                    CalOnClick(button.text.toString())
            )
        }
    }


    //继承OnClick接口
    internal inner class NumberOnClick(var Msg: String) : View.OnClickListener {
        override fun onClick(v: View) {
            TraceLog.i(Msg)
            if (output.text.toString() != "") {
                input.setText("")
                output.text = ""
            }
            input.append(Msg)
        }
    }

    internal inner class CalOnClick(var Msg: String) : View.OnClickListener {
        var calSymbol = arrayOf("+", "-", "*", "/", ".")
        override fun onClick(v: View) {
            TraceLog.i(Msg)
            if (!output.text.toString().equals("")) {
                input.setText("")
                output.text = ""
            }
            // 检查是否运算符重复输入
            for (i in calSymbol.indices) {
                if (Msg == calSymbol[i]) {
                    if (input.text.toString().split("")[input.text.toString().split("").size - 1] ==
                            calSymbol[i]) {
                        Msg = ""
                    }

                }
            }
            input.append(Msg)
        }
    }
}
