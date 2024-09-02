package com.sozge.routine_.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import androidx.room.Room
import com.sozge.routine_.databinding.FragmentTaskBinding
import com.sozge.routine_.model.Task

import com.sozge.routine_.roomdb.TaskDAO
import com.sozge.routine_.roomdb.TaskDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


class TaskFragment : Fragment() {
    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!

    private val  mDisposable = CompositeDisposable()
    private var  choose : Task? =null

    private lateinit var db: TaskDatabase
    private lateinit var taskDao: TaskDAO


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Room.databaseBuilder(requireContext(), TaskDatabase::class.java,"tasks")
            .build()
        taskDao = db.TaskDao()
        

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentTaskBinding.inflate(inflater,container, false)
        val view = binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.Save.setOnClickListener { save(it) }
        binding.Delete.setOnClickListener { delete(it) }
        binding.exit.setOnClickListener { exit(it) }

        arguments?.let {
            val information = TaskFragmentArgs.fromBundle(it).information

            if (information == "new") {

                choose = null

                binding.Delete.isEnabled = false
                binding.Save.isEnabled = true
            } else {
                binding.Save.isEnabled = true
                binding.Delete.isEnabled = true

                val id = TaskFragmentArgs.fromBundle(it).id

                mDisposable.add(
                    taskDao.findById(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this:: handleResponse)
                )
            }
        }

    }
    private fun handleResponse(task :Task) {
binding.titleText.setText(task.task)
        binding.descText.setText(task.desc)
        choose = task
    }
    fun exit(view: View) {
        val action = TaskFragmentDirections.actionTaskFragmentToListFragment()
        Navigation.findNavController(view).navigate(action)

    }
    fun save(view: View) {
        val task = binding.titleText.text.toString()
        val desc = binding.descText.text.toString()
        val Task = Task(task,desc)
        mDisposable.add(
            taskDao.insert(Task).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this:: handleResponseForInsert)
        )
    }

    private fun handleResponseForInsert() {
        val action = TaskFragmentDirections.actionTaskFragmentToListFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }

    fun delete(view: View) {
        if(choose != null) {
            mDisposable.add(
                taskDao.delete(task =choose!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleResponseForInsert)
            )
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding =null
        mDisposable.clear()
    }
}