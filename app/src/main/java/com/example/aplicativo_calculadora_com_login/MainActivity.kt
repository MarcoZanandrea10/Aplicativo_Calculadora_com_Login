package com.example.aplicativo_calculadora_com_login

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class MainActivity : Activity() {

    private val cpfCorreto = "12345678900"
    private val senhaCorreta = "123456"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editCpf = findViewById<EditText>(R.id.editCpf)
        val editSenha = findViewById<EditText>(R.id.editSenha)
        val btnEntrar = findViewById<Button>(R.id.btnEntrar)

        btnEntrar.setOnClickListener {
            val cpf = editCpf.text.toString().trim()
            val senha = editSenha.text.toString().trim()

            if (cpf == cpfCorreto && senha == senhaCorreta) {
                val intent = Intent(this, CalculatorActivity::class.java)
                startActivity(intent)
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Erro")
                    .setMessage("CPF ou senha inválidos.")
                    .setPositiveButton("OK", null)
                    .show()
            }
        }
    }
}