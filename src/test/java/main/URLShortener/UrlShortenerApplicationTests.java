package main.URLShortener;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class UrlShortenerApplicationTests {

	@Test
	void shouldReturnNullForUnknownCode() {
		Request result = memory.findReqWithCode("random code that's not stored");

		Assertions.assertNull(result);
	}

	@Test
	void shouldReturnCodeOfLengthEight() {
		int expectedLength = 8;

		String code = shortener.generateUrl();

		Assertions.assertEquals(expectedLength, code.length());
	}

	@Test
	void sameCodeShouldNotDuplicate() {
		String code = shortener.generateUrl();

		Request req = new Request(0, code, "https://example.com");

		memory.addURL(req);

		Request duplicatedReq = new Request(1, code, "https://example2.com");

		Response res = memory.addURL(duplicatedReq);

		Assertions.assertEquals(
				req,
				memory.findReqWithCode(code)
		);
	}

	@Test
	void canRetrieveInsertedRequest() {
		String code = shortener.generateUrl();
		Request test = new Request(0, code, "http://test.com");
		memory.addURL(test);

		Request result = memory.findReqWithCode(code);

		Assertions.assertEquals(result, test);
	}

	@ParameterizedTest
	@ValueSource(strings = {
			"hello",
			"   ",
			"htttp://1.com",
			"https://"
	})
	@EmptySource
	@NullSource
	void rejectsInvalidUrls(String url){
    	ResponseEntity res = isValid.validateURL(url);
		Assertions.assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
	}

	@ParameterizedTest
	@ValueSource(strings = {
			"https://example.com",
			"http://google.com",
			"https://example.com:8080/path",
			"HTTPS://example.com"
	})
	void acceptsValidUrls(String url) {
		ResponseEntity<?> res = isValid.validateURL(url);

		Assertions.assertEquals(
				HttpStatus.OK,
				res.getStatusCode()
		);
	}

}
