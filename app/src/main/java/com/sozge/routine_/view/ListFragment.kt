package com.sozge.routine_.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.sozge.routine_.R
import com.sozge.routine_.adapter.TaskAdapter
import com.sozge.routine_.databinding.FragmentListBinding
import com.sozge.routine_.model.Task
import com.sozge.routine_.roomdb.TaskDAO
import com.sozge.routine_.roomdb.TaskDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: TaskDatabase
    private lateinit var taskDao: TaskDAO
    private val mDisposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Room.databaseBuilder(requireContext(),TaskDatabase::class.java,"tasks").build()
        taskDao = db.TaskDao()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentListBinding.inflate(inflater,container, false)
        val view = binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newtask.setOnClickListener {newTask(it)}
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        getData()


    }
    private fun getData() {
        mDisposable.add(
            taskDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this:: handleResponse)
        )
    }

    private fun handleResponse(notes:List<Task>) {
        val adapter = TaskAdapter(notes )
        binding.recyclerView.adapter = adapter

    }



    fun newTask(view: View) {
        val action = ListFragmentDirections.actionListFragmentToTaskFragment(information = "new",id=0)
        Navigation.findNavController(view).navigate(action)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDisposable.clear()
    }


}