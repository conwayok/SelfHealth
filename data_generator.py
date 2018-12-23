import datetime
import random

days_to_generate = 3650
today = datetime.datetime.now().date()

file = open('selfHealth_%s.json' % today, 'w+')
file.write('[')
for i in range(days_to_generate):
    file.write('{')
    file.write('"dataDate":"%s"' % today)
    file.write(',')
    file.write('"hoursOfSleep":"%s"' % str(random.randint(2, 10)))
    file.write(',')
    file.write('"hoursPhoneUse":"%s"' % str(random.randint(1, 6)))
    file.write(',')
    file.write('"steps":"%s"' % str(random.randint(1000, 20000)))
    file.write(',')
    file.write('"userId":"id"')
    file.write(',')
    file.write('"waterCC":"%s"' % str(random.randint(500, 10000)))
    file.write('}')
    if i != days_to_generate - 1:
        file.write(',')
    today -= datetime.timedelta(days=1)
file.write(']')
file.close()
