package com.berkantcanerkanat.safepassword

import kotlin.random.Random


class PasswordGenerator {

    companion object {
        fun generatePassword(): String{
            val charGenerator =
                { alphabet: CharArray -> { count: Int -> (1..count).map { alphabet[Random.nextInt(0, alphabet.size)] } } }

            val randomDigits = charGenerator("0123456789".toCharArray())
            val randomLowecase = charGenerator("abcdefghijklmnopqrstuvwxyz".toCharArray())
            val randomUppercase = charGenerator("ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray())
            val randomSpecial = charGenerator("~@#$%^&*()!.".toCharArray())

            val password =
                (randomDigits(2) + randomLowecase(3) + randomUppercase(3) + randomSpecial(1))
                    .shuffled()
                    .joinToString(separator = "")

            return password
        }
    }

}