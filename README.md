# MTSDataTransferSwitcher
Joke application for switching mobile data connectivity with MTS contract plan by running USSD commands and switching mobile connectivity state

Mobile data connectivity state switched by running method of a field of ConnctivityManager using reflection. Does NOT work on Android version prior 2.3 and later 5.0. Not working on MIUI 6 (based on Android 4.3) either.

USSD commands:
*110*228# - check status of a service
*110*224# - turn on MTS Internet per day service
*110*234# - turn off MTS Interner per day service
