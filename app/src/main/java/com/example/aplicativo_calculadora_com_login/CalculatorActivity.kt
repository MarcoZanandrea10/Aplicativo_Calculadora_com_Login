package com.example.aplicativo_calculadora_com_login

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class CalculatorActivity : Activity() {

    private lateinit var editOperandoA: EditText
    private lateinit var editOperandoB: EditText
    private lateinit var textResultado: TextView

    private lateinit var btnSoma: Button
    private lateinit var btnSubtracao: Button
    private lateinit var btnMultiplicacao: Button
    private lateinit var btnDivisao: Button
    private lateinit var btnCompartilhar: Button
    private lateinit var btnVoltar: Button

    private var ultimoA = ""
    private var ultimoB = ""
    private var ultimoResultado = ""
    private var ultimaOperacaoTexto = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        editOperandoA = findViewById(R.id.editOperandoA)
        editOperandoB = findViewById(R.id.editOperandoB)
        textResultado = findViewById(R.id.textResultado)

        btnSoma = findViewById(R.id.btnSoma)
        btnSubtracao = findViewById(R.id.btnSubtracao)
        btnMultiplicacao = findViewById(R.id.btnMultiplicacao)
        btnDivisao = findViewById(R.id.btnDivisao)
        btnCompartilhar = findViewById(R.id.btnCompartilhar)
        btnVoltar = findViewById(R.id.btnVoltar)

        atualizarEstadoDosBotoes()

        editOperandoA.addTextChangedListener(watcher)
        editOperandoB.addTextChangedListener(watcher)

        btnSoma.setOnClickListener {
            calcular("mais") { a, b -> a + b }
        }

        btnSubtracao.setOnClickListener {
            calcular("menos") { a, b -> a - b }
        }

        btnMultiplicacao.setOnClickListener {
            calcular("vezes") { a, b -> a * b }
        }

        btnDivisao.setOnClickListener {
            val b = editOperandoB.text.toString().toDoubleOrNull()

            if (b == 0.0) {
                AlertDialog.Builder(this)
                    .setTitle("Erro")
                    .setMessage("Não é possível dividir por zero.")
                    .setPositiveButton("OK", null)
                    .show()
            } else {
                calcular("dividido por") { a, b -> a / b }
            }
        }

        btnVoltar.setOnClickListener {
            finish()
        }

        btnCompartilhar.setOnClickListener {
            compartilharResultado()
        }
    }

    private val watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            atualizarEstadoDosBotoes()
            limparResultado()
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private fun atualizarEstadoDosBotoes() {
        val temA = editOperandoA.text.toString().isNotBlank()
        val temB = editOperandoB.text.toString().isNotBlank()

        val habilitar = temA && temB

        btnSoma.isEnabled = habilitar
        btnSubtracao.isEnabled = habilitar
        btnMultiplicacao.isEnabled = habilitar
        btnDivisao.isEnabled = habilitar
    }

    private fun calcular(operacaoTexto: String, operacao: (Double, Double) -> Double) {
        val a = editOperandoA.text.toString().toDoubleOrNull()
        val b = editOperandoB.text.toString().toDoubleOrNull()

        if (a == null || b == null) {
            AlertDialog.Builder(this)
                .setTitle("Erro")
                .setMessage("Preencha os operandos corretamente.")
                .setPositiveButton("OK", null)
                .show()
            return
        }

        val resultado = operacao(a, b)

        ultimoA = formatarNumero(a)
        ultimoB = formatarNumero(b)
        ultimoResultado = formatarNumero(resultado)
        ultimaOperacaoTexto = operacaoTexto

        textResultado.text = ultimoResultado
        btnCompartilhar.visibility = View.VISIBLE
    }

    private fun limparResultado() {
        textResultado.text = "<resultado>"
        btnCompartilhar.visibility = View.GONE

        ultimoA = ""
        ultimoB = ""
        ultimoResultado = ""
        ultimaOperacaoTexto = ""
    }

    private fun compartilharResultado() {
        val mensagem = "Oi! Você sabia que $ultimoA $ultimaOperacaoTexto $ultimoB é igual a $ultimoResultado?"

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, mensagem)

        startActivity(Intent.createChooser(intent, "Compartilhar resultado"))
    }

    private fun formatarNumero(numero: Double): String {
        return if (numero % 1.0 == 0.0) {
            numero.toInt().toString()
        } else {
            numero.toString()
        }
    }
}