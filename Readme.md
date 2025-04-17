# Jobber

Jobber is a simple Java library which can be used to parse a resume and reformat it into an ATS compliant Word document. This library is built on top of [Langchain4j](https://github.com/langchain4j/langchain4j) and can be used to reformat and tailor resumes into specific job listings. It may operate with any LLMs provided by LangChain4j. I've tested with the following:

* ChatGPT 4.0
* Anthropic Claude 3.5 (Coming Soon)

The basic workflow for Jobber is as follows:

* Start with base "Uber" resume which is a handwritten in JSON or parsed from an existing document using the LLM
* Feed a job description, or its text, into the LLM to understand both the specific position as well as a company overview.
* Rewrite the original resume to match the specific terminology in the original job description.
* Generate a cover letter to fit the description.

## License

This is licensed under the [AGPL](https://www.gnu.org/licenses/agpl-3.0.txt). Other inquires, please email me directly.
