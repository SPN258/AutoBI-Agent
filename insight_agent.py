from agents.base_agent import BaseAgent
import json

class InsightAgent(BaseAgent):
    def analyze(self, query, data):
        system_msg = "You are a strategic business consultant. Summarize data results into a professional report."
        user_msg = f"User Request: {query}\nResult Data: {json.dumps(data)}\nGenerate a concise insight report."
        return self.llm.request(system_msg, user_msg)
