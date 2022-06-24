package eu.kanade.tachiyomi.extension.zh.imitui

import eu.kanade.tachiyomi.multisrc.sinmh.SinMH
import eu.kanade.tachiyomi.network.GET
import eu.kanade.tachiyomi.network.asObservableSuccess
import eu.kanade.tachiyomi.source.model.Page
import eu.kanade.tachiyomi.source.model.SChapter
import eu.kanade.tachiyomi.util.asJsoup
import org.jsoup.nodes.Document
import rx.Observable

class Imitui : SinMH("爱米推漫画", "https://www.imitui.com") {

    override fun fetchPageList(chapter: SChapter): Observable<List<Page>> =
        client.newCall(GET(baseUrl + chapter.url, headers)).asObservableSuccess().map {
            val pcResult = pageListParse(it)
            if (pcResult.isNotEmpty()) return@map pcResult
            val response = client.newCall(GET(mobileUrl + chapter.url, headers)).execute()
            mobilePageListParse(response.asJsoup())
        }

    private fun mobilePageListParse(document: Document): List<Page> {
        val pageCount = document.select("div.image-content > p").text().removePrefix("1/").toInt()
        val prefix = document.location().removeSuffix(".html")
        return (0 until pageCount).map { Page(it, url = "$prefix-${it + 1}.html") }
    }

    // mobile
    override fun imageUrlParse(document: Document): String =
        document.select("div.image-content > img#image").attr("src")
}