package com.example.mobile_clinicamedica

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mobile_clinicamedica.databinding.ActivityAppointmentBinding
import java.text.NumberFormat
import java.util.Locale

class AppointmentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppointmentBinding

    // Chaves usadas no putExtra / getExtra (evita erros de digitação)
    companion object {
        const val EXTRA_DOCTOR_ID = "extra_doctor_id"
        const val EXTRA_DOCTOR_NAME = "extra_doctor_name"
        const val EXTRA_DOCTOR_SPECIALTY = "extra_doctor_specialty"
        const val EXTRA_DOCTOR_PRICE = "extra_doctor_price"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Aplica os insets da barra de status para que o botão Voltar não fique coberto
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val statusBarInset = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.setPadding(
                view.paddingLeft,
                statusBarInset.top + 16,
                view.paddingRight,
                view.paddingBottom
            )
            insets
        }

        showDoctorData()

        // Botão de voltar simples
        binding.btnBack.setOnClickListener { finish() }

        // Interação 3: confirmar agendamento e retornar resultado para a MainActivity
        binding.btnConfirm.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra(EXTRA_DOCTOR_ID, intent.getIntExtra(EXTRA_DOCTOR_ID, -1))
                putExtra(EXTRA_DOCTOR_NAME, intent.getStringExtra(EXTRA_DOCTOR_NAME))
                putExtra(EXTRA_DOCTOR_SPECIALTY, intent.getStringExtra(EXTRA_DOCTOR_SPECIALTY))
            }
            setResult(RESULT_OK, resultIntent)
            Toast.makeText(this, "Consulta confirmada com sucesso!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    /** Lê os dados recebidos da Tela 1 e preenche a interface. */
    private fun showDoctorData() {
        val id = intent.getIntExtra(EXTRA_DOCTOR_ID, -1)
        // Operador elvis (?:) trata o caso de o valor vir nulo
        val name = intent.getStringExtra(EXTRA_DOCTOR_NAME) ?: "Médico não informado"
        val specialty = intent.getStringExtra(EXTRA_DOCTOR_SPECIALTY) ?: "Especialidade não informada"

        // Preço opcional: se não foi enviado, fica null
        val price: Double? =
            if (intent.hasExtra(EXTRA_DOCTOR_PRICE)) intent.getDoubleExtra(EXTRA_DOCTOR_PRICE, 0.0)
            else null

        binding.tvDoctorId.text = "Código: #$id"
        binding.tvDoctorName.text = name
        binding.tvDoctorSpecialty.text = specialty

        binding.tvPrice.text = if (price != null) {
            val formatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
            "Preço: ${formatter.format(price)}"
        } else {
            "Preço sob consulta"
        }
    }
}
