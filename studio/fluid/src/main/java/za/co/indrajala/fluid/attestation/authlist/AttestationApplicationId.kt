package za.co.indrajala.fluid.attestation.authlist

import org.bouncycastle.asn1.*
import za.co.indrajala.fluid.asn1.getBytes
import za.co.indrajala.fluid.ubyte.toHex

class AttestationApplicationId(
    octetString: ASN1OctetString
) {
    private val seq = ASN1InputStream(octetString.octets).readObject() as ASN1Sequence

    class Indices {
        companion object {
            val PackageInfos = 0
            val SignatureDigests = 1
        }
    }

    fun getHex(index: Int): String? =
        seq.getObjectAt(index).getBytes().toHex()

    val attPackageInfos: Set<AttestationPackageInfo>?
        get() = (seq.getObjectAt(Indices.PackageInfos) as ASN1Set?)?.map { AttestationPackageInfo(it.toASN1Primitive() as ASN1Sequence) }?.toSet()

    val signatureDigests: Set<String>?
        get() = (seq.getObjectAt(Indices.SignatureDigests) as ASN1Set?)?.map { it.getBytes().toHex() }?.toSet()

    fun summary(): List<Pair<String, String?>> {
        val base = listOf(
            Pair("Signature Digests", signatureDigests?.joinToString(", "))
        )

        val packInfos = attPackageInfos
        return if (packInfos == null)
             base
        else
            listOf(
                *base.toTypedArray(),
                Pair<String, String?>("Attestation Package Info(s):", ""),
                *packInfos.map { it.summary() }.flatten().toTypedArray()
            )
    }
}