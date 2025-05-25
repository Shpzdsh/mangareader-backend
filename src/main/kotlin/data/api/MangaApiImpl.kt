package ru.shpzdsh.data.api

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import ru.shpzdsh.data.models.Chapter
import ru.shpzdsh.data.models.Manga
import ru.shpzdsh.data.models.SearchResponse

class MangaApiImpl : MangaApi {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private companion object {
        const val BASE_URL = "https://zz.readmanga.io"
    }

    override suspend fun lastUpdated(): List<Manga> {
        val doc: Document = Jsoup.connect("$BASE_URL/list?sortType=updated").get()
//        println(doc)
        return extractMangasFromResponse(doc)

    }

    override suspend fun findMangasByName(name: String): List<Manga> {
        val doc: Document = Jsoup.connect("$BASE_URL/search/?q=$name").get()
        println(doc)
        return extractMangasFromResponse(doc)
    }

//    override suspend fun getMangaById(id: String): Manga {
//        val doc: Document = Jsoup.connect("$BASE_URL/$id").get()
//        return parseFullMangaDetails(doc, id)
//    }

    override suspend fun getMangaByName(name: String): Manga {
        val doc: Document = Jsoup.connect("$BASE_URL/$name").get()
//        println(doc)
        return parseFullMangaDetails(doc, name)
    }

    override suspend fun getMangaChapterByLink(
        mangaId: String?,
        chapterId: String?
    ): List<String> {
        val doc: Document = Jsoup.connect("$BASE_URL/manga/$mangaId/$chapterId").get()
        return extractChapterPages(doc)
    }

    override suspend fun getMangaByCategoryId(
        categoryId: String,
        page: Int
    ): SearchResponse {
        val doc: Document = Jsoup.connect("$BASE_URL/manga/?genres=$categoryId&page=$page").get()
        return SearchResponse(
            mangas = extractMangasFromCatalog(doc),
            pagesMax = calculateMaxPages(doc)
        )
    }

    override suspend fun getMangaByPopularity(page: Int): SearchResponse {
        val doc: Document = Jsoup.connect("$BASE_URL/manga/?ordering=-rating&page=$page").get()
        return SearchResponse(
            mangas = extractMangasFromCatalog(doc),
            pagesMax = calculateMaxPages(doc)
        )
    }

    private suspend fun extractMangasFromResponse(doc: Document): List<Manga> {
        return doc.select("div.tile.col-sm-6").map { item ->
            coroutineScope.async {
                Manga(
//                    id = extractIdFromUrl(item.select("a").attr("href")),
                    title = item.select("div.desc > h3").text(),
                    link = BASE_URL + item.select("a").attr("href"),
                    image = item.select("img").attr("data-original"),
                    description = item.select(" div.manga-description").text(),
                    updated = item.select("div.manga-updated").attr("title").toString(),
                )
            }
        }.awaitAll()
    }

    private fun extractIdFromUrl(url: String): String {
        return url.substringAfterLast("/manga/").substringBefore("/")
    }

//    private suspend fun parseFullMangaDetails(doc: Document, id: String): Manga {
//        val mainInfo = doc.selectFirst("div.manga-info")!!
//        val chapters = doc.select("div.chapter-list a").map { chapter ->
//            Chapter(
//                id = extractChapterId(chapter.attr("href")),
//                title = chapter.select("div.chapter-title").text(),
////                number = chapter.select("div.chapter-number").text(),
//                date = chapter.select("div.chapter-date").text(),
//                link = BASE_URL + chapter.attr("href")
//            )
//        }
//
//        return Manga(
//            id = id,
//            title = mainInfo.select("h1").text(),
//            description = mainInfo.select("div.description").text(),
//            image = mainInfo.select("img.cover img").attr("src"),
////            rating = mainInfo.select("div.rating-value").text().toFloatOrNull() ?: 0f,
//            genre = mainInfo.select("div.genres a").joinToString { it.text() },
//            status = mainInfo.select("div.status").text(),
//            author = mainInfo.select("div.author a").text(),
//            chapters = chapters,
//            views = mainInfo.selectFirst("div.views")?.text()?.substringBefore(" ") ?: "0"
//        )
//    }

    private fun parseFullMangaDetails(doc: Document, name: String): Manga {
        val mainInfo = doc.selectFirst("div.leftContent")!!
//        val meta = mainInfo.select("div.subject-actions-left a[title]")
        val chapters = doc.select("tr.item-row").map { chapter ->
            val linkElement = chapter.selectFirst("a.chapter-link")
            val dateElement = chapter.selectFirst("td.date")
            Chapter(
                id = chapter.attr("data-id") ?: "",
                title = linkElement?.text()?.trim() ?: "Без названия",
                date = dateElement?.attr("data-date-raw")?.substringBefore(" ") ?: dateElement?.text() ?: "Нет даты",
                link = linkElement?.attr("href") ?: "#",
            )
        }

        return Manga(
            title = doc.selectFirst("h1.names span.name")?.text()?.trim(),
            anotherTitle = doc.select("span.all-names-popover span.name").eachText().joinToString(", "),
            description = doc.select("meta[name=description]").attr("content"),
            rating = doc.select("meta[itemprop=ratingValue]").attr("content").toFloatOrNull() ?: 0f,
            genres = doc.select("div.genres a").eachText().joinToString(", "),
            image = mainInfo.select("div.picture-fotorama img[src]").attr("src") ?: "",
            status = doc.select("div.subject-actions-left a:contains(Статус)").text().takeIf { it.isNotBlank() } ?: "Unknown",
            author = doc.select("a[href^='/author/']").text().takeIf { it.isNotBlank() } ?: "Unknown",
            chapters = chapters,
//            views = parseViews(mainInfo)
        )
    }

    private fun parseViews(element: Element): Long {
        return element.select("span:contains(Просмотры) + span")
            .text()
            .replace(Regex("[^\\d]"), "")
            .toLongOrNull() ?: 0
    }

    private fun extractChapterId(url: String): String {
        return url.substringAfterLast("/")
    }

    private fun extractChapterPages(doc: Document): List<String> {
        return doc.select("div.page-container img.page-image").eachAttr("src")
    }

    private suspend fun extractMangasFromCatalog(doc: Document) : List<Manga> {
        return  doc.select("div.manga-list div.manga-item").map { item ->
            coroutineScope.async {
                Manga (
                    id = extractIdFromUrl(item.select("a").attr("href")),
                    title = item.select("div.manga-title").text(),
                    link = BASE_URL + item.select("a").attr("href")
                )
            }
        }.awaitAll()
    }

    private fun calculateMaxPages(doc: Document): Int {
        return doc.select("div.pagination a.page-link").lastOrNull()?.text()?.toIntOrNull() ?: 1
    }



}