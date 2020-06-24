# EkaitzHurtado_GrAL

Ekaitz Hurtadoren "Fog-in-the-loop aplikazioak garatzeko eta hedatzeko teknologien azterketa" GrAL-ean garatutako aplikazio guztien kodea biltegiratzeko errepositorioa da.

"Komunikazioa" karpetan FIL aplikazioen arteko komunikazioa aztertzeko garatu egin den jardueran sortu diren fitxategi guztiak daude.

  - Bi aplikazioen Dockerfile fitxategiak.
  - Bi aplikazioen programazio-kodea.
  - Osagai anitzeko kontenedorea sortzeko docker-compose.yml fitxategia.
  - Bi aplikazioak Docker kontenedore bihurtzerakoan liburutegiak instalatzeko fitxategiak.
  - "YAML" karpetan, aplikazioak Kubernetes klusterrean hedatzeko fitxategi guztiak daude.
  
"eXistManager" karpetan datu-base batez osatutako kontenedorearen jardueran garatutako fitxategiak daude.

  - AppToPut (EXistManagerren bertsio sinplea) aplikazioa kontenedore batera bihurtzeko Dockerfile fitxategia.
  - AppToPut aplikazioaren programazio-kodea.
  - ApptoPut aplikazioaren JAR fitxategia (normala eta ingurune-aldagaiak erabiltzen dituena).
  - Osagai anitzeko kontenedorea sortzeko docker-compose.yml fitxategia.
  - Igo egingo den argazkiaren fitxategia, SVG motakoa.
  - "YAML" karpetan, aplikazioak Kubernetes klusterrean hedatzeko fitxategi guztiak daude.
  
"eventManager" karpetan egoera baten aurrean osagai berriak sortzeko jardueran garatutako fitxategiak daude.

  - EventManager aplikazioa kontenedore batera bihurtzeko Dockerfile fitxategia.
  - EventManager aplikazioaren programazio-kodea.
  - EventManager aplikazioaren JAR fitxategia (K8s Client API liburutegia barnean duena).
  
Azkenik, Kubernetes instalatzeko Bash motako fitxategia dago, "k8s-pi_install.sh" izena duena.
