***How to run:
1.download project and open pom.xml
2.maven clean and install to make sure dependencies ready
3.run main function in Class TransactionProcessor

***How it works:
demo: https://www.youtube.com/watch?v=dVQ4ArdfOyw
Step1: prepare data-This monitoring system will build 10k transactions,
       and write to files/test.csv
Step2: Read data and calculate risk for each transaction, write result to CSV file


***Rules:
-Rule 1: if user blocked this merchant, return 1.0 risk score

-Rule 2: check unusual timeframe
        classify the activity into 5 windows, each window with a different risk score,
        eg. 1:00-7:00=>1.0, 7:00-8:00=>0.8, 8:00-10:00=>0.5, 10:00-23:00=>0, 23:00-1:00=>0.8

-Rule 3: return risk score according to transaction amount.
        too large or too small amount means larger risk.
        for example: amount between $0 and $1, return score 0.8

-Rule 4: check if the number is round, usually an authentic transaction to merchant should with decimal
        return 0.5 if the amount is round

-Rule 5: check if the user sends money to same merchant within a short time
        return 0.5 if 2 transactions found within 30 mins, return score 1.0 if 3 or more transactions