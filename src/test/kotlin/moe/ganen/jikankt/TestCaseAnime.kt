package moe.ganen.jikankt

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import moe.ganen.jikankt.connection.RestClient
import moe.ganen.jikankt.models.anime.*
import moe.ganen.jikankt.models.base.types.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class TestCaseAnime {

    //region getAnime

    @Test
    fun `test anime with correct ID`() {
        val expected = Anime(
            malId = 9289,
            title = "Hanasaku Iroha",
            titleJapanese = "花咲くいろは",
            titleSynonyms = null,
            status = "Finished Airing",
            aired = TimeInterval(simplifiedString = "Apr 3, 2011 to Sep 25, 2011"),
            related = RelatedAnime(adaptation = listOf(MalSubEntity(malId = 25408))),
            producers = listOf(MalSubEntity(malId = 23)),
            openingThemes = listOf("#1: \"Hana no Iro (ハナノイロ)\" by nano.RIPE (eps 2-13)"),
            endingThemes = listOf("#1: \"Hana no Iro (ハナノイロ)\" by nano.RIPE (ep 1)")
        )
        val result = runBlocking { JikanKt.apply { restClient = RestClient(url = "https://jikan.melijn.com/public/v3/") }.getAnime(9289) }

        assertEquals(expected.malId, result.malId)
        assertEquals(expected.title, result.title)
        assertEquals(expected.titleJapanese, result.titleJapanese)
        assertEquals(0, result.titleSynonyms?.count())
        assertEquals(expected.status, result.status)
        assertEquals(expected.aired?.simplifiedString, result.aired?.simplifiedString)
        assertEquals(expected.related?.adaptation?.get(0)?.malId, result.related?.adaptation?.get(0)?.malId)
        assertEquals(expected.producers?.get(0)?.malId, result.producers?.get(0)?.malId)
        assertEquals(expected.openingThemes?.get(0), result.openingThemes?.get(0))
        assertEquals(expected.endingThemes?.get(0), result.endingThemes?.get(0))

        runBlocking { delay(3000) }
    }

    @Test
    fun `test anime with bad ID`() {
        val result = runBlocking { JikanKt.getAnime(2) }

        assertNull(result.title)
        runBlocking { delay(3000) }
    }

    //endregion

    //region getAnimeCharactersStaff

    @Test
    fun `test Hanasaku Iroha characters staff`() {
        val expected = AnimeCharactersStaff(
            characters = listOf(
                Character(
                    malId = 36184,
                    name = "Matsumae, Ohana",
                    voiceActors = listOf(VoiceActor(malId = 762, name = "Itou, Kanae"))
                ),
                Character(
                    malId = 37215,
                    name = "Oshimizu, Nako",
                    voiceActors = listOf(VoiceActor(malId = 599, name = "Toyosaki, Aki"))
                )
            )
        )

        val result = runBlocking { JikanKt.getAnimeCharactersStaff(9289) }

        val firstExpected = expected.characters?.get(0)
        val firstResult = result.characters?.get(0)
        assertEquals(firstExpected?.malId, firstResult?.malId)
        assertEquals(firstExpected?.name, firstResult?.name)
        assertEquals(firstExpected?.voiceActors?.get(0)?.name, firstResult?.voiceActors?.get(0)?.name)

        val secondExpected = expected.characters?.get(1)
        val secondResult = result.characters?.get(1)
        assertEquals(secondExpected?.malId, secondResult?.malId)
        assertEquals(secondExpected?.name, secondResult?.name)
        assertEquals(secondExpected?.voiceActors?.get(0)?.name, secondResult?.voiceActors?.get(0)?.name)
        runBlocking { delay(3000) }
    }

    @Test
    fun `test Hinamatsuri characters staff`() {
        val expected = AnimeCharactersStaff(
            characters = listOf(
                Character(
                    malId = 100043,
                    name = "Hina",
                    voiceActors = listOf(VoiceActor(malId = 47440, name = "Tanaka, Takako"))
                ),
                Character(
                    malId = 100045,
                    name = "Nitta, Yoshifumi",
                    voiceActors = listOf(VoiceActor(malId = 20096, name = "Nakajima, Yoshiki"))
                )
            )
        )

        val result = runBlocking { JikanKt.getAnimeCharactersStaff(36296) }

        val firstExpected = expected.characters?.get(0)
        val firstResult = result.characters?.get(0)
        assertEquals(firstExpected?.malId, firstResult?.malId)
        assertEquals(firstExpected?.name, firstResult?.name)
        assertEquals(firstExpected?.voiceActors?.get(0)?.name, firstResult?.voiceActors?.get(0)?.name)

        val secondExpected = expected.characters?.get(1)
        val secondResult = result.characters?.get(1)
        assertEquals(secondExpected?.malId, secondResult?.malId)
        assertEquals(secondExpected?.name, secondResult?.name)
        assertEquals(secondExpected?.voiceActors?.get(0)?.name, secondResult?.voiceActors?.get(0)?.name)
        runBlocking { delay(3000) }
    }

    @Test
    fun `test anime with bad ID characters staff`() {
        val result = runBlocking { JikanKt.getAnimeCharactersStaff(2) }

        assertNull(result.characters)
        runBlocking { delay(3000) }
    }

    //endregion

    //region getAnimeEpisodes

    @Test
    fun `test Just Because Episodes`() {
        val expected = AnimeEpisodes(
            episodes = listOf(
                Episode(episodeId = 1, title = "On Your Marks!"),
                Episode(episodeId = 2, title = "Question")
            )
        )
        val result = runBlocking { JikanKt.getAnimeEpisodes(35639) }

        assertEquals(12, result.episodes?.count())
        assertEquals(expected.episodes?.get(0)?.title, result.episodes?.get(0)?.title)
        assertEquals(expected.episodes?.get(1)?.title, result.episodes?.get(1)?.title)
        runBlocking { delay(3000) }
    }

    @Test
    fun `test Kimi no Suizou wo Tabetai Episodes`() {
        val result = runBlocking { JikanKt.getAnimeEpisodes(36098) }

        assertEquals(0, result.episodes?.count())
        runBlocking { delay(3000) }
    }

    @Test
    fun `test One Piece Episodes`() {
        val expectedFirstPage = AnimeEpisodes(
            episodes = listOf(
                Episode(episodeId = 1, title = "I'm Luffy! The Man Who's Gonna Be King of the Pirates!")
            )
        )

        val resultFirstPage = runBlocking { JikanKt.getAnimeEpisodes(21) }

        assertEquals(10, resultFirstPage.lastPage)
        assertEquals(expectedFirstPage.episodes?.get(0)?.title, resultFirstPage.episodes?.get(0)?.title)

        val expectedSecondPage = AnimeEpisodes(
            episodes = listOf(
                Episode(episodeId = 101, title = "Showdown in a Heat Haze! Ace vs. the Gallant Scorpion!")
            )
        )

        val resultSecondPage = runBlocking { JikanKt.getAnimeEpisodes(21, 2) }

        assertEquals(10, resultSecondPage.lastPage)
        assertEquals(expectedSecondPage.episodes?.get(0)?.title, resultSecondPage.episodes?.get(0)?.title)
        runBlocking { delay(3000) }
    }

    @Test
    fun `test anime with bad ID episodes`() {
        val result = runBlocking { JikanKt.getAnimeEpisodes(2) }

        assert(result.episodes.isNullOrEmpty())
        runBlocking { delay(3000) }
    }

    //endregion

    //region getAnimeNews

    @Test
    fun `test Koe no Katachi news`() {
        val expected = AnimeNews(
            articles = listOf(
                Article(
                    url = "https://myanimelist.net/news/58614811",
                    intro = "Here are the North American anime & manga releases for November Week 1: November 5 - 11 Anime Releases Dragon Ball Z 30th Anniversary Collector's Edition Bl..."
                )
            )
        )
        val result = runBlocking { JikanKt.getAnimeNews(28851) }

        assertEquals(13, result.articles?.count())
        assertEquals(expected.articles?.get(0)?.url, result.articles?.get(0)?.url)
        assertEquals(expected.articles?.get(0)?.intro, result.articles?.get(0)?.intro)
        runBlocking { delay(3000) }
    }

    @Test
    fun `test anime with bad ID news`() {
        val result = runBlocking { JikanKt.getAnimeNews(2) }

        assertNull(result.articles)
        runBlocking { delay(3000) }
    }

    //endregion

    //region getAnimePictures

    @Test
    fun `test Kimi no Suizou wo Tabetai Pictures`() {
        val expected = AnimePictures(
            pictures = listOf(
                Picture(
                    "https://cdn.myanimelist.net/images/anime/11/90070l.jpg",
                    "https://cdn.myanimelist.net/images/anime/11/90070.jpg"
                )
            )
        )

        val result = runBlocking { JikanKt.getAnimePictures(36098) }

        assertEquals(4, result.pictures?.count())
        assertEquals(expected.pictures?.get(0), result.pictures?.get(0))
        runBlocking { delay(3000) }
    }

    @Test
    fun `test anime with bad ID pictures`() {
        val result = runBlocking { JikanKt.getAnimePictures(2) }

        assertNull(result.pictures)
        runBlocking { delay(3000) }
    }

    //endregion

    //region getAnimeVideo

    @Test
    fun `test Sora no Aosa wo Shiru Hito yo Video`() {
        val expected = AnimeVideos(
            promo = listOf(
                PromoVideo(
                    "Trailer 2",
                    "https://i.ytimg.com/vi/xhBQyCoE-dg/mqdefault.jpg",
                    "https://www.youtube.com/embed/xhBQyCoE-dg?enablejsapi=1&wmode=opaque&autoplay=1"
                )
            )
        )

        val result = runBlocking { JikanKt.getAnimeVideos(39569) }

        assertEquals(0, result.episodes?.count())
        assertEquals(4, result.promo?.count())
        assertEquals(expected.promo?.get(0), result.promo?.get(0))
        runBlocking { delay(3000) }
    }

    @Test
    fun `test anime with bad ID video`() {
        val result = runBlocking { JikanKt.getAnimeVideos(2) }

        assertNull(result.episodes)
        assertNull(result.promo)
        runBlocking { delay(3000) }
    }

    //endregion

    //region getAnimeStats

    @Test
    fun `test Sora no Aosa wo Shiru Hito yo Stats`() {
        val result = runBlocking { JikanKt.getAnimeStats(39569) }

        assert(result.watching != 0)
        assert(result.completed != 0)
        assert(result.onHold != 0)
        assert(result.dropped != 0)
        runBlocking { delay(3000) }
    }

    @Test
    fun `test anime with bad ID stats`() {
        val result = runBlocking { JikanKt.getAnimeStats(2) }

        assertNull(result.scorePage)
        runBlocking { delay(3000) }
    }

    //endregion

    //region getAnimeForum

    @Test
    fun `test Konosuba Kurenai Densetsu forum`() {
        val expected = AnimeForum(
            topics = listOf(
                Topic(
                    malId = 1800323,
                    title = "Kono Subarashii Sekai ni Shukufuku wo!: Kurenai Densetsu Episode 1 Discussion"
                )
            )
        )

        val result = runBlocking { JikanKt.getAnimeForum(38040) }

        assertEquals(15, result.topics?.count())
        assertEquals(expected.topics?.get(0)?.title, result.topics?.get(0)?.title)
        runBlocking { delay(3000) }
    }

    @Test
    fun `test anime with bad ID forum`() {
        val result = runBlocking { JikanKt.getAnimeForum(2) }

        assertNull(result.topics)
        runBlocking { delay(3000) }
    }

    //endregion

    //region getAnimeMoreInfo

    @Test
    fun `test Konosuba Kurenai Densetsu more info`() {
        val result = runBlocking { JikanKt.getAnimeMoreInfo(38040) }

        assertNull(result.moreInfo)
        runBlocking { delay(3000) }
    }

    @Test
    fun `test One Piece more info`() {
        val expected =
            "Episode 492 is the second part of a two part special called Toriko x One Piece Collabo Special - a crossover with Toriko (2011).  The first part is Toriko (2011) episode 1.  The first part aired on Toriko's timeslot at 9:00 and the second part aired on One Piece's timeslot at 9:30.\n" +
                    "Episode 542 is the second part of a two part special called Toriko x One Piece Collabo Special 2 - another crossover with Toriko (2011). The first part is Toriko (2011) episode 51. The first part aired on Toriko's timeslot at 9:00 and the second part aired on One Piece's timeslot at 9:30.\n" +
                    "Episode 590 is the second part of a two part special called Dream 9 Toriko & One Piece & Dragon Ball Z Super Collaboration Special - a crossover with Toriko (2011) and Dragon Ball Z. The first part is Toriko (2011) episode 99. The first part aired on Toriko's timeslot at 9:00 and the second part aired on One Piece's timeslot at 9:30.\n" +
                    "(Source: AniDB)\n" +
                    "The broadcast of the series' 930th episode was postponed due to COVID-19. From April 26, 2020, the series has been rebroadcasting from episode 892—the beginning of the Wano Country Arc—until further notice."
        val result = runBlocking { JikanKt.getAnimeMoreInfo(21) }

        assertEquals(expected, result.moreInfo)
        runBlocking { delay(3000) }
    }

    @Test
    fun `test anime with bad ID more info`() {
        val result = runBlocking { JikanKt.getAnimeMoreInfo(2) }

        assertNull(result.moreInfo)
        runBlocking { delay(3000) }
    }

    //endregion

    //region getAnimeReviews

    @Test
    fun `test Nichijou reviews`() {
        val expectedFirstPage = AnimeReviews(
            reviews = listOf(
                Review(
                    malId = 44337,
                    reviewer = Reviewer(username = "Ryhzuo"),
                    content = "Conventional wisdom has always taught us that more is usually better. We think that the more expensive car should have more completely unrelated features"
                )
            )
        )
        val resultFirstPage = runBlocking { JikanKt.getAnimeReviews(10165) }

        assertEquals(expectedFirstPage.reviews?.get(0)?.malId, resultFirstPage.reviews?.get(0)?.malId)
        assertEquals(
            expectedFirstPage.reviews?.get(0)?.reviewer?.username,
            resultFirstPage.reviews?.get(0)?.reviewer?.username
        )
        assert(
            resultFirstPage.reviews?.get(0)?.content?.contains(
                expectedFirstPage.reviews?.get(0)?.content.toString(),
                false
            ) ?: false
        )

        val expectedSecondPage = AnimeReviews(
            reviews = listOf(
                Review(
                    malId = 36849,
                    reviewer = Reviewer(username = "AliceMargatroid"),
                    content = "Most anime I watch are completed simply for the sake of completing it, because I made a vow to myself never to list an anime in my own list as \\\"dropped\\\" the moment I started it.\\r\\nNichijou will never be one of those types of anime. It is amazing. Never in my life have I watched an anime filled with so much randomness. Even Lucky Star which has held the undisputed king of randomness anime spot on my list has been dethroned within minutes of the first episode being aired.\\r\\nThis is my first review BTW so forgive me if this review sucks or anything. All theratings are just my personal opinion\\\\n\\\\n\\r\\nStory: 10/10\\\\n\\r\\nI give 10/10 in story for an anime which has no story, but this is precisely why it is so damn amazing. There are jokes placed at almost every corner of each episode, with the timing of each being so perfect that I wonder how much time the producers take to come up with each joke at each precise timing. And where else can one find anime that has people bringing in machine guns to school, riding a goat to school, or even wrestling a deer in school? And I haven't even gone to what happens outside of school. I have found myself laughing time and again that my mother has knocked on my door plenty of times to find me in a laughing fit. \\\\n\\\\n\\r\\nArt: 9/10\\\\n\\r\\nThe art is not perfect, I've seen animes with slightly better art than this, but I find this kind of artwork suits this anime very well. It looks simple at times but it fits in very well, while scenes that required plenty of details are, well, very detailed. I find myself to be rather amazed by the artwork at times.\\\\n\\\\n\\r\\nSound: 10/10\\\\n\\r\\nWith such a wide cast of characters it's gotta be hard to find good VAs for every character who can bring out their true personality and colour, but thankfully they have done it. Most characters, if not all, have VAs that have successfully portrayed their characters style and personality, while others have not gotten enough air time for me to determine that. The BGM is great too, often going well with the scene that is going at that moment.\\\\n\\\\n\\r\\nCharacter: 10/10\\\\n\\r\\nI love the characters here. Yuuko as the dumb one, Mio as the sensible one always keeping Yuuko in check, and Mai as the silent one who always own Yuuko (and sometimes Mio) in whatever she does. These 3 make the perfect combination within the confines of their classroom. Outside the classroom, however, we have Hakase the childish one (literally), Nano the caretaker robot who is oft abused by Hakase, and Sakamoto the cat as the one maintaining the sensibility within the household. And not forgetting the side characters, the teacher (whose name I don't remember) who is scared of students, the goat rider whose looks reminds me of Katsuragi Keima of The World God Only Knows, the mohawk guy, and the principal, among many others. All of these characters converge together to make one of the most entertaining animes I have ever watched. \\\\n\\\\n\\r\\nP.S. Sakamoto is my favourite. The moment he made his introductory speech I knew I was going to love him, and he hasn't disappointed\\\\n\\\\n\\r\\nEnjoyment: 10/10\\\\n\\r\\nNeed I say more? This entire review has been about how great this show is and how much I enjoyed it.\\\\n\\\\n\\r\\nOverall: 10/10\\\\n\\r\\nThis anime is very very much enjoyable, but it probably is not suited for people who don't like randomness anime, who'd rather be able to tell what's going to happen next. This is personal preference, and as a person who watches a lot of such animes filled with randomness, I have to say it's one of the best such animes I've watched."
                )
            )
        )
        val resultSecondPage = runBlocking { JikanKt.getAnimeReviews(10165, 2) }

        assertEquals(expectedSecondPage.reviews?.get(0)?.malId, resultSecondPage.reviews?.get(0)?.malId)
        assertEquals(
            expectedSecondPage.reviews?.get(0)?.reviewer?.username,
            resultSecondPage.reviews?.get(0)?.reviewer?.username
        )
        assert(
            resultSecondPage.reviews?.get(0)?.content?.contains(
                expectedSecondPage.reviews?.get(0)?.content.toString(),
                false
            ) ?: false
        )
        runBlocking { delay(3000) }
    }

    @Test
    fun `test anime with bad ID reviews`() {
        val result = runBlocking { JikanKt.getAnimeReviews(2) }

        assertNull(result.reviews)
        runBlocking { delay(3000) }
    }

    //endregion

    //region getAnimeRecommendations

    @Test
    fun `test No Game No Life recommendations`() {
        val expected = AnimeRecommendations(
            recommendations = listOf(
                Recommendation(
                    malId = 15315,
                    title = "Mondaiji-tachi ga Isekai kara Kuru Sou Desu yo?"
                )
            )
        )
        val result = runBlocking { JikanKt.getAnimeRecommendations(19815) }

        assertEquals(expected.recommendations?.get(0)?.malId, result.recommendations?.get(0)?.malId)
        assertEquals(expected.recommendations?.get(0)?.title, result.recommendations?.get(0)?.title)
        runBlocking { delay(3000) }
    }

    @Test
    fun `test anime with bad ID recommendations`() {
        val result = runBlocking { JikanKt.getAnimeRecommendations(2) }

        assertNull(result.recommendations)
        runBlocking { delay(3000) }
    }

    //endregion

    //region getAnimeUserUpdates

    @Test
    fun `test No Game No Life user updates`() {
        val resultFirstPage = runBlocking { JikanKt.getAnimeUserUpdates(19815) }

        assertEquals(75, resultFirstPage.updates?.count())

        val resultSecondPage = runBlocking { JikanKt.getAnimeUserUpdates(19815, 2) }

        assertEquals(75, resultSecondPage.updates?.count())
        runBlocking { delay(3000) }
    }

    @Test
    fun `test anime with bad ID user update`() {
        val result = runBlocking { JikanKt.getAnimeUserUpdates(2) }

        assertNull(result.updates)
        runBlocking { delay(3000) }
    }

    //endregion

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setup() {
            JikanKt.apply { restClient = RestClient(url = "https://jikan.melijn.com/public/v3/") }
        }
    }
}