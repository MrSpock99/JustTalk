package itis.ru.justtalk.ui.words.test

interface Subject {
    fun register(observer: Observer)
    fun unregister(observer: Observer)
    fun notifyObserversHintClicked(clicked: Boolean)
}
