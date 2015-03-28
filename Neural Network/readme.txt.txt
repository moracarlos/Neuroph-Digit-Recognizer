Trainer: Clase encargada del entrenamiento de la red neuronal. Se usan 1000 imagenes de la base de
datos MNIST para entrenarla. Cada una de estas imagenes tiene 28x28 pixeles de ancho y alto.

Tester: Clase encargada de la pruba imagen por imagen. Se puede usar cualquier tamano de pbm pero
el rendimiento siempre sera mejor con imagenes iguales (o aproximadas) a 28x28 pixeles.

La clase trainer contiene un archivo llamado Log40.txt, ahi se encuentra la fecha de iniciacion del 
entrenamiento, se muestra la evolucion con 40 neuronas ocultas y finalmente se muestra la fecha de finalizacion
del entrenamiento.

El directorio imagenes contiene algunas pruebas hechas a la red.

Para entrenar la red, dirigirse a Trainer/dist y ejecutar "java -jar Trainer.jar"
Generará el archivo .NNET que debe ser copiado en el directorio Tester/dist para hacer la prediccion del numero.

Para ejecutar la predicción: En el directorio Tester/dist ejecutar "java -jar Tester.jar" y seleccionar el archivo .pbm

Carlos Mora - 23595856
Karina De Sousa - 23696989