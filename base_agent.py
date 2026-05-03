from core.llm_client import LLMService

class BaseAgent:
    def __init__(self):
        self.llm = LLMService()
