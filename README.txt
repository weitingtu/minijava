Java source files obtained from
http://www.cambridge.org/resources/052182060X/

minijava.jj obtained from
http://www.cambridge.org/resources/052182060X/lecturers/default.htm

Type check

- Array index or size is not an integer

- Condition of an “if-else” or “while” statement is not a “boolean” value

- Assigning a true/false to an int variable

- Assigning an object X to a variable of type Y but X is not an instance of Y

- Comparing a boolean value to an int value

Rules when int is mixed with double

- When an operand to a binary operator is of type int and the other operand is of type double, then the operand of type int should be promoted to an equivalent value of type double.

- When a value is of type int but type double is expected, the value of type int can be safely promoted to an equivalent value of type double. (e.g., assigning int value to a double variable)
