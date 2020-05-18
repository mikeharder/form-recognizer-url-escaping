package com.mycompany.app;

// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

import com.azure.ai.formrecognizer.*;
import com.azure.ai.formrecognizer.models.OperationResult;
import com.azure.ai.formrecognizer.models.RecognizedForm;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.util.IterableStream;
import com.azure.core.util.polling.SyncPoller;

public class App {

    public static void main(String[] args) {
        String endpoint = System.getenv("FORM_RECOGNIZER_ENDPOINT");
        String apiKey = System.getenv("FORM_RECOGNIZER_API_KEY");
        String modelId = System.getenv("UNLABELED_CUSTOM_MODEL_ID");
        String escapedUrl = System.getenv("ESCAPED_URL");
        String unescapedUrl = System.getenv("UNESCAPED_URL");

        // Instantiate a client that will be used to call the service.
        FormRecognizerClient client = new FormRecognizerClientBuilder()
            .credential(new AzureKeyCredential(apiKey))
            .endpoint(endpoint)
            .buildClient();

        test(client, modelId, escapedUrl);
        test(client, modelId, unescapedUrl);

        System.exit(0);
    }

    public static void test(FormRecognizerClient client, String modelId, String url) {
        try {
            SyncPoller<OperationResult, IterableStream<RecognizedForm>> recognizeFormPoller =
            client.beginRecognizeCustomFormsFromUrl(url, modelId);

            IterableStream<RecognizedForm> recognizedForms = recognizeFormPoller.getFinalResult();

            System.out.println(recognizedForms.stream().count());
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}