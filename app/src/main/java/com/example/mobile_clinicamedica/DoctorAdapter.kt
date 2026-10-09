package com.example.mobile_clinicamedica

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_clinicamedica.databinding.ItemDoctorBinding

/**
 * Adapter simples do RecyclerView.
 * Recebe a lista de médicos e uma função (lambda) que é chamada
 * quando o botão "Agendar" de algum item é clicado.
 */
class DoctorAdapter(
    private val doctors: List<Doctor>,
    private val onScheduleClick: (Doctor) -> Unit
) : RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {

    // ViewHolder usando ViewBinding do item_doctor.xml
    class DoctorViewHolder(val binding: ItemDoctorBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val binding = ItemDoctorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DoctorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val doctor = doctors[position]

        holder.binding.tvItemName.text = doctor.name
        holder.binding.tvItemSpecialty.text = doctor.specialty

        // Se o médico não estiver disponível, desabilita o botão
        holder.binding.btnSchedule.isEnabled = doctor.isAvailable

        holder.binding.btnSchedule.setOnClickListener {
            onScheduleClick(doctor)
        }
    }

    override fun getItemCount(): Int = doctors.size
}
