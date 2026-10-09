package com.example.mobile_clinicamedica

/**
 Modelo de dados IMUTÁVEL (todas as propriedades são "val").
 price é opcional (Double?): pode ser null quando o preço é "sob consulta".
 isAvailable tem valor padrão true.
 */
data class Doctor(
    val id: Int,
    val name: String,
    val specialty: String,
    val price: Double?,
    val isAvailable: Boolean = true
)