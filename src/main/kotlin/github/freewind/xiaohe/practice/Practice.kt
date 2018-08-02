package github.freewind.xiaohe.practice

import tornadofx.*

class HelloWorld : View() {
    private val label = label("Hello, TornadoFX!")

    override val root = vbox {
        menubar {
            menu("Menu") {
                item("Change Text").setOnAction { _ ->
                    label.text = "Changed"
                }
            }
        }
        add(label)
    }
}

class HelloWorldStyle : Stylesheet() {
    init {
        root {
            prefWidth = 400.px
            prefHeight = 400.px
            fontSize = 20.px
        }
    }
}

class HelloWorldApp : App(HelloWorld::class, HelloWorldStyle::class)

fun main(args: Array<String>) {
    launch<HelloWorldApp>()
}