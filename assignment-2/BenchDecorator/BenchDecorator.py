import json
import time
import threading
import statistics
from functools import wraps


def bench(n_threads=1, seq_iter=1, iter=1):
    """Decorator used to do the benchmark of functions in Python using Multithreading.

    :param  n_threads: the number of threads (default: 1).
    :type n_threads: int
    :param seq_iter:  the number of times fun must be invoked in each thread (default: 1).
    :type seq_iter: int
    :param iter: times fun is repeated. The execution time is computed for each execution(default: 1).
    :type iter: int
    :return: returns a dictionary containing the results of the benchmark with the given parameters.
    """

    def decorator(func):
        @wraps(func)
        def wrapper(*args, **kwargs):
            results = []

            for _ in range(iter):
                threads = []

                def worker():
                    for _ in range(seq_iter):
                        func(*args, **kwargs)

                start_time = time.perf_counter()
                for _ in range(n_threads):
                    thread = threading.Thread(target=worker)
                    threads.append(thread)

                for thread in threads:
                    thread.start()

                for thread in threads:
                    thread.join()
                end_time = time.perf_counter()
                execution_time = end_time - start_time
                results.append(execution_time)

            average_execution_time = statistics.mean(results)

            if iter == 1:
                variance = 0
            else:
                variance = statistics.variance(results)

            result_dict = {
                'fun': func.__name__,
                'args': args,
                'n_threads': n_threads,
                'seq_iter': seq_iter,
                'iter': iter,
                'mean': average_execution_time,
                'variance': variance
            }

            return result_dict

        return wrapper

    return decorator


def just_wait(n):  # NOOP for n/10 seconds
    time.sleep(n * 0.1)


def grezzo(n):  # CPU intensive
    for i in range(2 ** n):
        pass


def test(iter, fun, args):
    """ Function to test functions in Python with multithreading.

    :param iter: number of iterations.
    :type iter: int
    :param fun: function to be tested.
    :type fun: callable
    :param args: args for function fun.
    :type args: tuple
    :return: output files with results of the tests.
    """

    for n_threads in [1, 2, 4, 8]:
        seq_iter = 16 // n_threads
        result = bench(n_threads=n_threads, seq_iter=seq_iter, iter=iter)(fun)(*args)

        filename = f"results/{fun.__name__}_{args}_{n_threads}_{seq_iter}.txt"
        print(result)
        print(filename)
        with open(filename, 'w') as file:
            file.write(str(json.dumps(result, indent=4)))


if __name__ == '__main__':
    test(iter=16, fun=just_wait, args=(5,))
    test(iter=16, fun=grezzo, args=(5,))

# the test shows as expected that the performance of the program:
# - decreases with the increase of the number of threads on cpu intensive functions like grezzo (the more threads
#   the more time it takes to complete the execution)
# - increases on busy waiting functions like just_wait (the more threads the less time it takes to complete the
#   execution)
# This is caused by the GIL (Global Interpreter Lock) that prevents multiple threads from executing Python bytecodes,
# so much of the parallelism on multiprocessor machines is lost.
