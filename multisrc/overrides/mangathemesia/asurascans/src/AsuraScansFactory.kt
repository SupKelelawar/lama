package eu.kanade.tachiyomi.extension.all.asurascans

import eu.kanade.tachiyomi.source.SourceFactory
import eu.kanade.tachiyomi.source.model.SManga
import java.text.SimpleDateFormat
import java.util.Locale

class AsuraScansFactory : SourceFactory {
    override fun createSources() = listOf(
        AsuraScansEn(),
        AsuraScansTr()
    )
}

class AsuraScansEn : AsuraScans("https://www.asurascans.com", "en", SimpleDateFormat("MMM d, yyyy", Locale.US)) {

    override val seriesDescriptionSelector = "div.desc p, div.entry-content p, div[itemprop=description]:not(:has(p))"
    override val seriesTypeSelector = "${super.seriesTypeSelector}, .tsinfo .imptdt:contains(type) a"
    override val pageSelector = "div.rdminimal > img, div.rdminimal > p > img, div.rdminimal > a > img, div.rdminimal > p > a > img"
}

class AsuraScansTr : AsuraScans("https://tr.asurascans.com", "tr", SimpleDateFormat("MMM d, yyyy", Locale("tr"))) {

    override val seriesArtistSelector = ".fmed b:contains(Çizer)+span"
    override val seriesAuthorSelector = ".fmed b:contains(Yazar)+span"
    override val seriesStatusSelector = ".imptdt:contains(Durum) i"
    override val seriesTypeSelector = ".imptdt:contains(Tür) a"

    override val altNamePrefix: String = "Alternatif isim: "

    override fun String?.parseStatus(): Int = when {
        this == null -> SManga.UNKNOWN
        this.contains("Devam Ediyor", ignoreCase = true) -> SManga.ONGOING
        this.contains("Tamamlandı", ignoreCase = true) -> SManga.COMPLETED
        else -> SManga.UNKNOWN
    }
}
