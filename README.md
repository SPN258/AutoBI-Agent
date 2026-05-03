# Enterprise BI Agent Framework

An automated data insight system based on Multi-Agent collaboration, designed to bridge the gap between natural language queries and complex SQL execution with a self-healing reflection mechanism.

## Features
- **Intent Routing**: Automatically maps business queries to database schemas.
- **Self-Healing SQL Generation**: Built-in reflection loop to fix SQL errors dynamically (up to 3 retries).
- **RAG-based Metadata Retrieval**: Enhances prompt context with enterprise-level data dictionaries.
- **Business Insight Generation**: Summarizes raw data into actionable business reports.

## Architecture
1. **ParserAgent**: Interprets user intent and retrieves relevant metadata.
2. **CoderAgent**: Generates optimized SQL based on retrieved schemas.
3. **ValidatorAgent**: Executes SQL in a sandbox and analyzes execution logs.
4. **ReporterAgent**: Performs data aggregation and narrative generation.

## Quick Start
1. Install dependencies: `pip install -r requirements.txt`
2. Configure your API in `core/config.py`
3. Run the service: `python main.py`
