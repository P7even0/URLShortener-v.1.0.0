package main.URLShortener;

import main.URLShortener.Controllers.urlController;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

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

	@Test
	void threadsShouldNotCreateDuplicates() throws InterruptedException, ExecutionException{
		memory.clearHashMap();
		List<Future<ResponseEntity<?>>> finalResults = new ArrayList<>();
		ExecutorService executor = Executors.newFixedThreadPool(10);

		for (int i = 0; i < 100; i++){
			Future<ResponseEntity<?>> future = executor.submit(() -> {
				// This can be replaced by memory.shortenerUrl()
				String code = "test123";
				ResponseEntity<?> res = urlController.concurrencyCreateNewUrl(code, "http://example.com");


				return res;
			});


			finalResults.add(future);
		}

		executor.shutdown();


		for (Future<ResponseEntity<?>> future : finalResults){
			ResponseEntity<?> response = future.get();

			Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
		}
		boolean finished = executor.awaitTermination(10, TimeUnit.SECONDS);

		Assertions.assertTrue(finished);

		// Assert all 100 requests were saved
		Assertions.assertEquals(100, memory.getLength());



	}


}

