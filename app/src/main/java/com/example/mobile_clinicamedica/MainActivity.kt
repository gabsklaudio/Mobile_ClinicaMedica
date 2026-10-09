package com.example.mobile_clinicamedica

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile_clinicamedica.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val doctors = listOf(
        Doctor(1, "Dra. Ana Souza", "Cardiologia", 250.0),
        Doctor(2, "Dr. Carlos Lima", "Dermatologia", 180.0),
        Doctor(3, "Dra. Beatriz Rocha", "Pediatria", null),
        Doctor(4, "Dr. Paulo Mendes", "Ortopedia", 300.0, false)
    )

    private val scheduledDoctorIds = mutableSetOf<Int>()

    // Launcher para receber a confirmação de agendamento vinda da AppointmentActivity
    private val appointmentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val doctorId = result.data?.getIntExtra(AppointmentActivity.EXTRA_DOCTOR_ID, -1) ?: -1
            val doctorName = result.data?.getStringExtra(AppointmentActivity.EXTRA_DOCTOR_NAME) ?: ""
            val specialty = result.data?.getStringExtra(AppointmentActivity.EXTRA_DOCTOR_SPECIALTY) ?: ""

            if (doctorId != -1) {
                scheduledDoctorIds.add(doctorId)
                updateNextAppointmentCard(doctorName, specialty)
                Toast.makeText(
                    this,
                    "Agendamento com $doctorName registrado! (Total agendados: ${scheduledDoctorIds.size})",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        setupCheckIn()
        setupDoctorList()
    }

    private fun setupCheckIn() {
        binding.btnCheckIn.setOnClickListener {
            binding.tvStatus.text = "Check-in Realizado"
            binding.btnCheckIn.isEnabled = false
        }
    }

    private fun setupDoctorList() {
        binding.rvDoctors.layoutManager = LinearLayoutManager(this)
        binding.rvDoctors.adapter = DoctorAdapter(doctors) { doctor ->
            openAppointment(doctor)
        }
    }

    private fun updateNextAppointmentCard(doctorName: String, specialty: String) {
        binding.tvNextDoctorName.text = doctorName
        binding.tvNextSpecialty.text = specialty
        binding.tvDateTime.text = "Consulta confirmada"
        binding.tvStatus.text = "Aguardando check-in"
        binding.btnCheckIn.isEnabled = true
    }

    private fun openAppointment(doctor: Doctor) {
        val intent = Intent(this, AppointmentActivity::class.java).apply {
            putExtra(AppointmentActivity.EXTRA_DOCTOR_ID, doctor.id)
            putExtra(AppointmentActivity.EXTRA_DOCTOR_NAME, doctor.name)
            putExtra(AppointmentActivity.EXTRA_DOCTOR_SPECIALTY, doctor.specialty)
            doctor.price?.let { putExtra(AppointmentActivity.EXTRA_DOCTOR_PRICE, it) }
        }
        appointmentLauncher.launch(intent)
    }
}