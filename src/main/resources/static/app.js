function attachEvents() {
    const btnLoadAuthor = document.getElementById('loadAuthors');
    let tableElRef = document.getElementById('authors-container');
    let tableBookElRef=document.getElementById('books-container')


    btnLoadAuthor.addEventListener('click', (e) => {
        e.preventDefault()
        fetch('http://localhost:8080/authors')
            .then(response => response.json())

            .then((json) => json.forEach((author, idx) => {


                let rowEl = document.createElement('tr');

                let cellElId = document.createElement('td');
                cellElId.textContent = `${author.id}`;
                rowEl.appendChild(cellElId);
                tableElRef.appendChild(rowEl);

                let cellElName = document.createElement('td');
                cellElName.textContent = `${author.name}`;
                rowEl.appendChild(cellElName);


                let cellElActionsShow = document.createElement('td');
                let showBookBtn = document.createElement('button');
                showBookBtn.textContent = 'Show books';
                cellElActionsShow.appendChild(showBookBtn);
                rowEl.appendChild(cellElActionsShow);

                showBookBtn.addEventListener('click', () => {


                        let authorId = author.id;
                        fetch(`http://localhost:8080/authors/${authorId}/books`)
                            .then(response => response.json())

                            .then((json) => json.forEach((book,idx)=>{
                               //  You can do something with the books here.
                               //  For example, log the books to the console:
                                //console.log(book.title);

                                let rowBookEl = document.createElement('tr');


                                let cellElBooks = document.createElement('td');
                                cellElBooks.textContent = `${book.title}`;
                                rowBookEl.appendChild(cellElBooks);
                                tableBookElRef.appendChild(rowBookEl);


                                let cellElBookDelete=document.createElement('td');
                                let deleteBookBtn = document.createElement('button');
                                deleteBookBtn.textContent = 'Delete this book';
                                cellElBookDelete.appendChild(deleteBookBtn);
                                rowBookEl.appendChild(cellElBookDelete);
                                deleteBookBtn.addEventListener('click', () => {
                                    fetch(`http://localhost:8080/books/${book.id}`, {
                                        method: 'DELETE'
                                    })
                                        .then(response => response.json())
                                        .then(() => {
                                            rowBookEl.remove();
                                        })
                                })

                            }))


                    }

                )






                let cellElActions = document.createElement('td');
                let deleteBtn = document.createElement('button');
                deleteBtn.textContent = 'Delete';

                deleteBtn.addEventListener('click', () => {
                    fetch(`http://localhost:8080/authors/${author.id}`, {
                        method: 'DELETE'
                    })
                        .then(response => response.json())
                        .then(() => {
                            rowEl.remove();
                        })
                })
                cellElActions.appendChild(deleteBtn);
                rowEl.appendChild(cellElActions);


            }))
        tableElRef.innerHTML = ''

    })


}

attachEvents();



