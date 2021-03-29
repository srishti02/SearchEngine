package pageParser;

/**
 * @description A singleton class to perform parsing operations on a url page.
 *              Class shall provide access to htmlToText conversion, database
 *              connection as well as cache mechanism.
 *
 *              If crawling is enabled, this class shall call DbHandler to
 *              insert a web page data in db.
 *              If crawling is disabled, this class will cache html data from db.
 *
 *              Other functions of class:
 *              - Using HtmlToText to get base data for pattern matching.
 *              - Providing data to page ranker.
 */
public class PageParser {

}
