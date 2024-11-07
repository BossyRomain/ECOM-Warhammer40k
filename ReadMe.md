# ECOM

## Compilation

Pour compiler le projet :

      mvn clean package

## Lancement

### Environnement de développement

Sur Linux:

      java -jar /chemin/vers/le/jar -Dspring.profiles.active=dev

Sur Windows:

      java -jar /chemin/vers/le/jar --spring.profiles.active=dev 

### Environnement de production

Même commande que pour l'environnement de développement mais replacer **dev** par **prod**.
