const { FormRecognizerClient, AzureKeyCredential } = require("@azure/ai-form-recognizer");

async function main() {
  const endpoint = process.env["FORM_RECOGNIZER_ENDPOINT"] || "<cognitive services endpoint>";
  const apiKey = process.env["FORM_RECOGNIZER_API_KEY"] || "<api key>";
  const modelId = process.env["UNLABELED_CUSTOM_MODEL_ID"] || "<unlabeled custom model id>";
  const escapedUrl = process.env["ESCAPED_URL"] || "<escaped url>";
  const unescapedUrl = process.env["UNESCAPED_URL"] || "<unescaped url>";

  const client = new FormRecognizerClient(endpoint, new AzureKeyCredential(apiKey));

  await test(client, modelId, escapedUrl);
  await test(client, modelId, unescapedUrl);
}

async function test(client, modelId, url) {
  try {
    const poller = await client.beginRecognizeCustomFormsFromUrl(modelId, url, {
      onProgress: (state) => {
        console.log(`status: ${state.status}`);
      }
    });
    await poller.pollUntilDone();
    const response = poller.getResult();
  
    if (!response) {
      throw new Error("Expecting valid response!");
    }
  
    console.log(response.status);
  
    console.log("Errors:");
    console.log(response.errors);
  }
  catch (err) {
    console.error(err);
  }
}

main().catch((err) => {
  console.error("The sample encountered an error:", err);
});