## CSI 3120 Assignment 2 - Syntax Analyzer
### Mershab Issadien
#### 300027272

Simple syntax analyzer written in Java.

Test Instructions:
Run SyntaxAnalyzerTest class, it will read all the test resources in `test_resources` folder and execute the SyntaxAnalyzer on all these test cases.

Notes:
- I have a complaint about the input test files. 
    - They are not standardized at all. Some `expected_output.txt` files have `Syntax analysis failed` and some have `Syntax analysis failed.`
    - In addition, some outputs would be `syntax_analyzer_error - Missing ( at line x` and some would be `syntax_analyzer_error - Missing '(' at line x`
    - Above are just a few examples of the output massaging that I had to do. While these may not be dealbreakers by any means, TDD development relies solely on the quality of the test data given. If it is inconsistent then the solution will be inconsistent as well.
- In addition, I implore you to read the test case outputs throughly, For the most part the diagnosis is correct, only the line numbers are incorrect.
