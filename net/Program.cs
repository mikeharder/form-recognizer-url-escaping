using Azure;
using Azure.AI.FormRecognizer;
using System;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;

namespace FormRecUrlEscape
{
    class Program
    {
        static async Task Main(string[] args)
        {
            var endpoint = Environment.GetEnvironmentVariable("FORM_RECOGNIZER_ENDPOINT");
            var apiKey = Environment.GetEnvironmentVariable("FORM_RECOGNIZER_API_KEY");
            var modelId = Environment.GetEnvironmentVariable("UNLABELED_CUSTOM_MODEL_ID");
            var escapedUrl = Environment.GetEnvironmentVariable("ESCAPED_URL");
            var unescapedUrl = Environment.GetEnvironmentVariable("UNESCAPED_URL");

            var credential = new AzureKeyCredential(apiKey);
            var client = new FormRecognizerClient(new Uri(endpoint), credential);

            await Test(client, modelId, escapedUrl);
            await Test(client, modelId, unescapedUrl);
        }

        static async Task Test(FormRecognizerClient client, string modelId, string url) {
            try {
                var forms = await client.StartRecognizeCustomFormsFromUriAsync(modelId, new Uri(url))
                    .WaitForCompletionAsync();
                Console.WriteLine(forms.Value.Count);
            }
            catch (Exception e) {
                Console.WriteLine(e);
            }
        }
    }
}
