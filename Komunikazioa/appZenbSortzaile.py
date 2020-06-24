from random import randint
import time, requests, os
import simplejson as json

kont = 1

def zenbakiaSortu():
    zenbakia = randint(1,100)
    print("Sortu den ausazko zenbakia: " + str(zenbakia))
    return zenbakia
    
    
def zenbakiaBidali():
    time.sleep(10)    # Hasieran 10 seg itxaron
    global kont
    while(kont < 11):    # 10 iterazio
        print("Iterazioa: " + str(kont))
        randomNum = zenbakiaSortu()
        headers = {'Content-Type': 'application/json'}
        params = {'zenbakia': randomNum}
        # Ingurune-aldagaien balioak lortu
        url = 'http://'+ os.environ.get('APPBATUKETA_SERVICE_HOST') +':'+ os.environ.get('APPBATUKETA_SERVICE_PORT')
        # POST motako HTTP mezua bidali	
        r = requests.post(url, headers=headers, data=json.dumps(params))
        if(r.status_code != 200):
            print("!!!!!!!!!!!!!!!!!!!!!!!!")
            print("KONTUZ!   Mezua ez da ondo bidali")
        kont = kont + 1
        time.sleep(5)
    

print("ZenbSortzaile aplikazioa hasi egin da")
zenbakiaBidali()