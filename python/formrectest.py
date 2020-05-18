import os

class RecognizeCustomForms(object):

    endpoint = os.environ["AZURE_FORM_RECOGNIZER_ENDPOINT"]
    key = os.environ["AZURE_FORM_RECOGNIZER_KEY"]
    model_id = os.environ["CUSTOM_TRAINED_MODEL_ID"]

    escaped_url = os.environ["ESCAPED_URL"]
    unescaped_url = os.environ["UNESCAPED_URL"]

    def recognize_custom_forms(self):
        # [START recognize_custom_forms]
        from azure.core.credentials import AzureKeyCredential
        from azure.ai.formrecognizer import FormRecognizerClient
        form_recognizer_client = FormRecognizerClient(
            endpoint=self.endpoint, credential=AzureKeyCredential(self.key)
        )
        self.test(form_recognizer_client, self.escaped_url)
        self.test(form_recognizer_client, self.unescaped_url)

    def test(self, client, url):
        try:
            poller = client.begin_recognize_custom_forms_from_url(model_id=self.model_id, url=url)
            forms = poller.result()
            print(len(forms))
        except Exception as err:
            print(err)


if __name__ == '__main__':
    sample = RecognizeCustomForms()
    sample.recognize_custom_forms()