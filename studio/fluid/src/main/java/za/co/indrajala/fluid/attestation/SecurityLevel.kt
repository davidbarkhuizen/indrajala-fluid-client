package za.co.indrajala.fluid.attestation

enum class SecurityLevel(val value: Int) {
    Software  (0),
    TrustedEnvironment  (1),
    StrongBox  (2);

    companion object {
        private val map = SecurityLevel.values().associateBy(SecurityLevel::value)
        fun fromValue(value: Int): SecurityLevel {
            return map[value] ?: error("$value")
        }
    }
}