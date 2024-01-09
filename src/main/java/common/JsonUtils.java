package common;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonUtils {

	public boolean checkErrorMessage(String responseBody, String expectedMessage) {
		String[] splittedResponseBody = responseBody.split("\"");
		boolean isChecked = false;
		for (int i = 0; i < splittedResponseBody.length; i++) {
			if (splittedResponseBody[i].equals(expectedMessage)) {
				isChecked = true;
			}
		}
		return isChecked;
	}

	// Get value from Json body by key body.status
	public static String getValueByKeyMitl(String request, String key) {
		String value = "";
		String[] splitKey = key.split("\\.");
		JSONObject responseBodyObj = jsonToObject(request);
		JSONObject parentOfKey = responseBodyObj;
		for (int i = 0; i < splitKey.length - 1; ++i) {
			String subKey = splitKey[i];
			Object subValue = parentOfKey.get(subKey);
			if (subValue instanceof JSONObject) {
				parentOfKey = (JSONObject) subValue;
			}
		}
		if (parentOfKey == null) {
			throw new RuntimeException("key sai rồi bạn ơi");
		}
		value = parentOfKey.get(splitKey[splitKey.length - 1]).toString();
		return value;
	}

	public String getValueByKey(String responseBody, String key) {
		//JSONParser parser = new JSONParser();
		String value = "";
		try {
			JSONObject responseBodyObj = jsonToObject(responseBody);
			value = responseBodyObj.get(key).toString();
		} catch (Exception e) {
			System.out.println("Response body is null.");
			e.printStackTrace();
		}
		return value;
	}

	public String replateJSONObjectValue(File file, String key, String value) {
		String resultFile = null, filePath = file.getAbsolutePath(), fileToStringFile;
		try {
			fileToStringFile = new String(Files.readAllBytes(Paths.get(filePath)));
			JSONObject request = jsonToObject(fileToStringFile);
			String[] splitKey = key.split("\\.");
			JSONObject parentOfKey = request;
			for (int i = 0; i < splitKey.length - 1; ++i) {
				String subKey = splitKey[i];
				Object subValue = parentOfKey.get(subKey);
				if (subValue instanceof JSONObject) {
					parentOfKey = (JSONObject) subValue;
				}
			}
			if (parentOfKey == null) {
				// "truyen sai key roi"
				return null;
			}
			// check data truyen vao
			if (value.equals("missing")) {
				parentOfKey.remove(splitKey[splitKey.length - 1]);
			} else if (value.equals("\"\"")) {
				parentOfKey.put(splitKey[splitKey.length - 1], "");
			} else if (value.equals("null")) {
				parentOfKey.put(splitKey[splitKey.length - 1], null);
			} else if (isNumeric(value)) {
				parentOfKey.put(splitKey[splitKey.length - 1], Double.parseDouble(value));
			} else {
				parentOfKey.put(splitKey[splitKey.length - 1], value);
			}
			resultFile = request.toJSONString();
		} catch (Exception e) {
			System.out.println("File not found");
		}
		return resultFile;
	}

	// code c Luy
	public void copyJsonFile(File sourceFile, File destinationFile) {
		if (destinationFile.exists()) {
			destinationFile.delete();
		}
		try {
			Files.copy(sourceFile.toPath(), destinationFile.toPath());
			System.out.println("Copy successfully");
		} catch (Exception e) {
			System.out.println("Json request body is not found");
		}
	}

	public String changeValueByFieldName(File file, String fieldName, String value) {
		String resultFile = null;
		String filePath = file.getAbsolutePath();
		try {
			String originalFile = new String(Files.readAllBytes(Paths.get(filePath)));

			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(originalFile);
			if (value.equals("missing")) {
				jsonObject.remove(fieldName);
			} else if (value.equals("null")) {
				jsonObject.put(fieldName, null);
			} else if (value.equals("true")) {
				jsonObject.put(fieldName, true);
			} else if (value.equals("\"\"")) {
				jsonObject.put(fieldName, "");
			} else if (isNumeric(value)) {
				jsonObject.put(fieldName, Double.parseDouble(value));
			} else {
				jsonObject.put(fieldName, value);
			}
			resultFile = jsonObject.toJSONString();

		} catch (Exception e) {
			System.out.println("File not found");
		}

		return resultFile;

	}

	public String readJsonFile(String filePath) {
		String fileContent = "";
		Path path = Paths.get(filePath);
		try {
			fileContent = new String((Files.readAllBytes(path)));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fileContent;
	}

	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			double d = Double.parseDouble(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static JSONObject jsonToObject(String json) {
		JSONParser parser = new JSONParser();
		try {
			return (JSONObject) parser.parse(json);
		} catch (ParseException e) {
			return null;
		}
	}
}
