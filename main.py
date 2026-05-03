import logging
from agents.sql_agent import SQLAgent
from agents.insight_agent import InsightAgent

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger("BI-Agent")

class BIWorkflow:
    def __init__(self):
        self.sql_agent = SQLAgent()
        self.insight_agent = InsightAgent()
        self.schema_mock = "Table: orders (id, user_id, amount, status, created_at); Table: users (id, name, level);"

    def run(self, user_query):
        logger.info(f"Starting workflow for: {user_query}")
        
        attempt = 0
        error_context = None
        sql = ""
        
        while attempt < 3:
            sql = self.sql_agent.generate(user_query, self.schema_mock, error_context)
            # Mocking SQL execution with a failure on the first try to demonstrate reflection
            if "JOIN" not in sql.upper() and attempt == 0:
                logger.warning(f"Attempt {attempt + 1} failed. Triggering reflection...")
                error_context = {"sql": sql, "error": "Unknown column 'level' in order table"}
                attempt += 1
                continue
            
            # Successful Mock Result
            mock_data = [{"city": "Beijing", "total": 5000}, {"city": "Shanghai", "total": 7000}]
            logger.info("SQL Execution successful.")
            return self.insight_agent.analyze(user_query, mock_data)

if __name__ == "__main__":
    app = BIWorkflow()
    report = app.run("Show total sales amount by city for VIP users")
    print("\n--- FINAL REPORT ---\n")
    print(report)
