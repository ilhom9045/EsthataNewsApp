package tj.esthata.newsapp.others

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tj.esthata.newsapp.repository.networkrepository.event.Event

typealias MyMutableLiveData<T> = MutableLiveData<Event<T>>
typealias MyLiveData<T> = LiveData<Event<T>>