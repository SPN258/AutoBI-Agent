import os
from pydantic_settings import BaseSettings

class Settings(BaseSettings):
    API_KEY: str = "your_api_key_here"
    BASE_URL: str = "https://api.openai.com/v1"
    MODEL: str = "gpt-4-turbo"
    MAX_RETRIES: int = 3
    DB_TYPE: str = "mysql"

settings = Settings()
