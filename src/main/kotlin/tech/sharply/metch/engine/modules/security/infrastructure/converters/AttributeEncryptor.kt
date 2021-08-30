package tech.sharply.metch.engine.modules.security.infrastructure.converters

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.InvalidKeyException
import java.security.Key
import java.util.*
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.spec.SecretKeySpec
import javax.persistence.AttributeConverter
import javax.persistence.Converter


@Converter
@Component
class AttributeEncryptor(
    @Value("\${data.encryption.secret:155ACCE4399CE93B9D736CC47788F}")
    private val encryptionSecret: String
) : AttributeConverter<String, String> {

    private val key: Key
    private val cipher: Cipher

    init {
        key = SecretKeySpec(encryptionSecret.toByteArray(), AES)
        cipher = Cipher.getInstance(AES)
    }

    companion object {
        private const val AES = "AES"
    }

    override fun convertToDatabaseColumn(attribute: String): String {
        return try {
            cipher.init(Cipher.ENCRYPT_MODE, key)
            Base64.getEncoder().encodeToString(cipher.doFinal(attribute.toByteArray()))
        } catch (e: IllegalBlockSizeException) {
            throw IllegalStateException(e)
        } catch (e: BadPaddingException) {
            throw IllegalStateException(e)
        } catch (e: InvalidKeyException) {
            throw IllegalStateException(e)
        }
    }

    override fun convertToEntityAttribute(dbData: String): String {
        return try {
            cipher.init(Cipher.DECRYPT_MODE, key)
            String(cipher.doFinal(Base64.getDecoder().decode(dbData)))
        } catch (e: InvalidKeyException) {
            throw IllegalStateException(e)
        } catch (e: BadPaddingException) {
            throw IllegalStateException(e)
        } catch (e: IllegalBlockSizeException) {
            throw IllegalStateException(e)
        }
    }

}