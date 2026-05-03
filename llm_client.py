from openai import OpenAI
from core.config import settings

class LLMService:
    def __init__(self):
        self.client = OpenAI(api_key=settings.API_KEY, base_url=settings.BASE_URL)

    def request(self, system_prompt, user_prompt):
        response = self.client.chat.completions.create(
            model=settings.MODEL,
            messages=[
                {"role": "system", "content": system_prompt},
                {"role": "user", "content": user_prompt}
            ],
            temperature=0.1
        )
        return response.choices[0].message.content
