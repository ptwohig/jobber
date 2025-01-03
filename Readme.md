# Jobber

Jobber is a simple Java library which can be used to parse a resume and reformat it into an ATS compliant Word document. This library is built on top of [Langchain4j](https://github.com/langchain4j/langchain4j) and can be used to reformat and tailor resumes into specific job listings. It may operate with any LLMs provided by LangChain4j. I've tested with the following:

* ChatGPT 4.0
* Anthropic Claude 3.5

The basic workflow for Jobber is as follows:

* Start with base "Uber" resume which is a handwritten in JSON or parsed from an existing document using the LLM
* Feed a job description, or its text, into the LLM to understand both the specific position as well as a company overview.
* Rewrite the original resume to match the specific terminology in the original job description.
* Generate a cover letter to fit the description.

## License

This is licensed under the [AGPL](https://www.gnu.org/licenses/agpl-3.0.txt). This means that if you use this code for any purpose you must make available 100% of your application's source code. I do intend and have the means to enforce this license if necessary.

I am doing this because I believe ATS, in its current form, is overall bad for the job market. Having been a startup founder, hiring manager, and jobseeker over the last twenty years it's become obvious that such automated systems do a disservice to both employers and employees. Applicants are forced to tune their resume on an individual basis just to pass the first round of weeding-out while employers often times disqualified perfectly fine applicants of simple mistakes that the applicant doesn't know (see the illusion of choice fallacy).

Automation clearly hasn't helped. And with may technologies not fully thought out, this hurts the applicants (and working class) more than it hurts employers.

If you wish to use this for closed source projects, please email me and I am happy relicense this code under the Apache at no charge provided the following terms are met:

* You are not developing ATS or similar applicant processing systems.
* You are an educational, non-profit, or otherwise charitable or humanitarian institution.
* You are using it for strictly personal uses.

For any other inquiries, I'm happy to discuss further.