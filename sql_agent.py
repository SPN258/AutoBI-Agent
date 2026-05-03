from agents.base_agent import BaseAgent

class SQLAgent(BaseAgent):
    def generate(self, query, schema, error_context=None):
        if error_context:
            system_msg = "You are an expert SQL debugger. Fix the provided SQL based on the error log."
            user_msg = f"Schema: {schema}\nFailed SQL: {error_context['sql']}\nError: {error_context['error']}\nProvide ONLY the corrected SQL."
        else:
            system_msg = "You are a senior data analyst. Generate efficient SQL for the given schema and query."
            user_msg = f"Schema: {schema}\nQuery: {query}\nReturn raw SQL only."
        
        res = self.llm.request(system_msg, user_msg)
        return res.replace("```sql", "").replace("```", "").strip()
