package moe.ganen.jikankt

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import moe.ganen.jikankt.connection.RestClient
import moe.ganen.jikankt.exception.JikanException
import moe.ganen.jikankt.models.base.types.Picture
import moe.ganen.jikankt.models.character.Character
import moe.ganen.jikankt.models.character.CharacterPictures
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TestCaseCharacter {

    @Test
    fun `test get Kaori Miazono`() {
        val expected = Character(
            malId = 69411,
            name = "Kaori Miyazono",
            nameKanji = "宮園 かをり",
            about = "\"Age: 14-15\\\\n\\r\\nBirthday: July 4\\\\n\\\\n\\r\\nKaori Miyazono is one of the main protagonists of Shigatsu wa Kimi no Uso. She is a violinist who helps Kousei Arima return to the piano world after his mother's death."
        )

        val result = runBlocking { JikanKt.getCharacter(69411) }

        assertEquals(expected.malId, result.malId)
        assertEquals(expected.name, result.name)
        assertEquals(expected.nameKanji, result.nameKanji)
        assert(result.about?.contains(result.about.toString(), false) ?: false)
        assert(result.nicknames.isNullOrEmpty())

        runBlocking { delay(3000) }
    }

    @Test
    fun `test get character with bad ID`() {
        val result = runBlocking {
            JikanKt.apply { restClient = RestClient(false, url = "https://jikan.melijn.com/public/v3/") }.getCharacter(10)
        }

        assert(result.name.isNullOrEmpty())

        runBlocking { delay(3000) }
    }

    @Test
    fun `test get character with bad ID then throw exception`() {
        assertThrows<JikanException> {
            runBlocking {
                JikanKt.apply { restClient = RestClient(true, url = "https://jikan.melijn.com/public/v3/") }.getCharacter(10)
            }
        }

        runBlocking { delay(3000) }
    }

    @Test
    fun `test get Kaori Miazono pictures`() {
        val expected = CharacterPictures(
            pictures = listOf(
                Picture(
                    "https://cdn.myanimelist.net/images/characters/10/252839.jpg",
                    "https://cdn.myanimelist.net/images/characters/10/252839.jpg"
                ),
                Picture(
                    "https://cdn.myanimelist.net/images/characters/7/264841.jpg",
                    "https://cdn.myanimelist.net/images/characters/7/264841.jpg"
                ),
                Picture(
                    "https://cdn.myanimelist.net/images/characters/14/269375.jpg",
                    "https://cdn.myanimelist.net/images/characters/14/269375.jpg"
                )
            )
        )

        val result = runBlocking { JikanKt.getCharacterPictures(69411) }

        for (i in 0..2) {
            assertEquals(expected.pictures?.get(i), result.pictures?.get(i))
        }

        runBlocking { delay(3000) }
    }

    @Test
    fun `test get character picture with bad ID`() {
        val result = runBlocking {
            JikanKt.apply { restClient = RestClient(false, url = "https://jikan.melijn.com/public/v3/") }.getCharacterPictures(10)
        }

        assert(result.pictures.isNullOrEmpty())

        runBlocking { delay(3000) }
    }

    @Test
    fun `test get character picture with bad ID then throw exception`() {
        val result = runBlocking {
            JikanKt.apply { restClient = RestClient(true, url = "https://jikan.melijn.com/public/v3/") }.getCharacterPictures(10)
        }

        assert(result.pictures.isNullOrEmpty())

        runBlocking { delay(3000) }
    }

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setup() {
            JikanKt.apply { restClient = RestClient(url = "https://jikan.melijn.com/public/v3/") }
        }
    }
}