package com.examples.uidcreator

import com.examples.uidcreator.util.SecurityUtils
import io.restassured.RestAssured
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.When
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.io.File
import java.io.FileWriter

@SpringBootTest
class UiCreationTest {

    @Test
    fun uidCreationTest() {
        RestAssured.baseURI = "http://localhost:8083/"
        for (k in 0..19) {
            val privateKey = "tawWsDKHfH1QXHlM6rZB7w2G2ijNvXAkuSB5ERo$k"
            val response = Given {
                contentType("text/plain")
            } When {
                get("uidcreator/key?privateKey=$privateKey")
            }
            response.statusCode == 200;//Mock server had not started yet
        }
        val privateKey = "tawWsDKHfH1QXHlM6rZB7w2G2ijNvXAkuSB5ERo0"
        val request =
            "{\"reason\":\"CREATE_USER\",\"email\":\"39DPW3BPCA@test.com\",\"first_name\":null,\"last_name\":null,\"uid\":null,\"social_linking\":null}"
        val data = SecurityUtils.encryptAndHMAC(privateKey, request)

        val response2 = Given {
            contentType("text/plain")
        } When {
            get("uidcreator?data=$data")
        }
        System.out.println(response2.body().asString())
        response2.statusCode == 200;//Mock server had not started yet
        Assertions.assertEquals(
            "{\"uid\":\"39DPW3BPCAtestcom\",\"registration\":false,\"passwordless\":false}",
            response2.body.asString()
        )
    }

    @Test
    fun generateUids() {
        RestAssured.baseURI = "http://localhost:8083/"
        val fileKeys = File("../jmeter/keys.csv")
        var outKeysFile = FileWriter(fileKeys)
        var keys = HashSet<String>()
        for (k in 0..7) {
            val privateKey = "tawWsDKHfH1QXHlM6rZB7w2G2ijNvXAkuSB5ERo$k"
            keys.add(privateKey)
            if (k != 0) {
                outKeysFile.write("\n")
            }
            outKeysFile.write("$privateKey")
        }
        outKeysFile.close()

        val file = File("../jmeter/requests.csv")
        var outFile = FileWriter(file)
        for (k in 0..100000) {
            val request =
                "{\"reason\":\"CREATE_USER\",\"email\":\"39DPW3BPCA$k@test.com\",\"first_name\":null,\"last_name\":null,\"uid\":null,\"social_linking\":null}"
            var privateKey = keys.random()
            val data = SecurityUtils.encryptAndHMAC(privateKey, request)
            if (k != 0) {
                outFile.write("\n")
            }
            outFile.write("$data, $privateKey")
        }
        outFile.close()
    }


}
