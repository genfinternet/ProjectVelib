Projet Android Semestre 1
=========================


Le but du projet est de développé deux écrans :
·      Un écran affichant la liste des stations de velib.
·      Un écran affichant les détails de chaque station de velib.

Afin d’afficher ces informations, voici le Webservice que vous pouvez utiliser : [getVelibStations](https://opendata.paris.fr/api/records/1.0/search/?dataset=stations-velib-disponibilites-en-temps-reel&rows=100&facet=banking&facet=bonus&facet=status&facet=contract_name)

# Conditions :

·      Gestion des versions d’Android entre 4.1 (Api 16) et 7.1.1 (Api 25)
·      Gestion du smartphone et tablette.
·      Gestion des deux orientations (paysage ou portrait).
·      Gestion du mode connecté / non connecté.
·      Vous pouvez utiliser toutes les bibliothèques que vous voulez.

# Ecran 1 : La liste des velibs

La premier affichera la liste des stations velib (avec leur nom – champs « name ») et une icône pour dire si la station est ouverte ou non (champs « status »).

Lors du clic sur l’une des stations de la liste, le détail de la station s’affiche dans un nouvel écran.

Cette écran possédera une Toolbar avec deux boutons :
·      Un bouton permettant de recherche une station de velib selon son adresse.
·      Un bouton qui affiche la liste des membres du groupe.

# Ecran 2 : Détail d’une station de velib

Cet écran affiche les détails d’une station velib :
·      Nom de la station,
·      Status (ouvert / fermé),
·      Nombre de place dans la station (« bike_stands »),
·      Nombre de place disponible dans la station (« available_bike_stands »),
·      Adresse
·      Date de mise à jour des informations.

Vous pouvez passer d’une station à une autre (la précédente et la suivante dans la liste) en swipant de droite à gauche dans les détails.

Si l’utilisateur clic sur l’adresse de la station, l’application « Google Maps » s’ouvre avec la position de la station.

Cette écran possédera une Toolbar avec deux boutons :
·      Un bouton permettant de partager la station de velib via IntentChooser (Email, Twitter, SMS …).
·      Un bouton qui affiche la liste des membres du groupe.

# Notation :

Voici les critères qui seront pris en compte dans la notation :
·      Respect des deadlines de rendu (très important).
·      Respect des consignes.
·      Respect des fonctionnalités demandés.
·      Design.
·      Bugs dans l’application.
·      Crash.
·      Code.

# Bonus :

Vous avez la possibilité de faire tous les bonus que vous voulez. Il y aura des catégories de bonus :
·      Les bonus fonctionnels : Intégrer Google Map, Animations … etc.
·      Les bonus techniques : Tests, Persistance de données … etc.

# Deadline

Vous devez m’envoyer un email à [benbourahla.nazim@gmail.com](mailto:benbourahla.nazim@gmail.com) Le dimanche 11 Juin au plus tard à 23h59. (Aucun rendu en retard ne sera accepté).

L’émail aura comme titre : [EPITA - MTI][Projet – Rendu]

L’émail devra absolument contenir :
·      Un APK fonctionnel.
·      Un lien vers le code source de l’application (lien github, dropbox, drive …).
·      Le code doit absolument compiler.

Pour toutes questions ou demande de précision, envoyez-moi un email avec le titre suivant : [EPITA - MTI][Projet – Question]
