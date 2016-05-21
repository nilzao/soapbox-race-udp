how the protocol works.

using netcat client

    $ nc -u localhost 9998

---
1st send hello packet

    hello! sidx:X sid:YYY
    
    where X is user session index (0-9) and YYY is sessionId (integer 32k)

examples (user 1, 2 and 5 talking inside session 333) one netcat each

    hello! sidx:1 sid:333
    hello! sidx:2 sid:333
    hello! sidx:5 sid:333
---
2nd send the sync packet before reachs 10 sec:

    any text with "gogogo!"

examples

    we are here gogogo! for it!
    gogogo!
---
testing packages counting

msg:

    any text with "msg:"

examples:

    msg: hello world!
    we are talking "msg:" here

ping:

    any text with "ping:"

examples:
    
    ping: hello there
    we are ping: ing this
