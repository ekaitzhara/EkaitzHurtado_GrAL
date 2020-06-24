from flask import Flask, request, jsonify
import sys

app = Flask(__name__)
batuketaTotala = 0
kont = 0

@app.route('/', methods=['POST', 'GET'])
def index():
    global kont
    global batuketaTotala
    # Lortutako HTTP mezua POST motakoa bada
    if request.method == 'POST':
        zenbakia = request.json['zenbakia']
        print('Lortutako zenbakia: ' + str(zenbakia), file=sys.stderr)
        batuketaTotala = batuketaTotala + zenbakia
        print('Batuketa osoa: ' + str(batuketaTotala), file=sys.stderr)
        kont = kont + 1
        if(kont == 10):
            print('########################', file=sys.stderr)
            print('Azken emaitza: ' + str(batuketaTotala), file=sys.stderr)
            print('########################', file=sys.stderr)
        return jsonify("Azken emaitza: " + str(batuketaTotala))

    # Lortutako HTTP mezua GET motakoa bada
    if request.method == 'GET':
        print("Orain arteko batuketa: " + str(batuketaTotala), file=sys.stderr)
        print("Zein iterazioan gaude: " + str(kont), file=sys.stderr)
        return jsonify("Orain arteko batuketa: " + str(batuketaTotala))