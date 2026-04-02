import React from 'react';
import { Note } from '../types';
import { FiEye, FiEdit2, FiTrash2, FiArchive, FiUnlock } from 'react-icons/fi'; // Feather icons

interface NoteListProps {
  notes: Note[];
  onView: (note: Note) => void;
  onEdit: (note: Note) => void;
  onDelete: (id: number) => void;
  onArchive: (id: number) => void;
  onUnarchive: (id: number) => void;
}

const NoteList: React.FC<NoteListProps> = ({ notes, onView, onEdit, onDelete, onArchive, onUnarchive }) => {
  return (
    <div>
      {notes.length === 0 ? (
        <p>No notes found.</p>
      ) : (
        <table>
          <thead>
          <tr>
            <th>Title</th>
            <th>Content</th>
            <th>Categories</th>
            <th>Actions</th>
          </tr>
          </thead>
          <tbody>
          {notes.map(note => (
            <tr key={note.id}>
              <td>{note.title}</td>
              <td>{note.content.length > 100 ? note.content.substring(0, 100) + '...' : note.content}</td>
              <td>{note.categories.map(c => c.name).join(', ')}</td>
              <td>
                <div className="action-buttons">
                  <button className="btn-view" onClick={() => onView(note)} title="View">
                    <FiEye /> <span className="btn-text">View</span>
                  </button>
                  <button className="btn-edit" onClick={() => onEdit(note)} title="Edit">
                    <FiEdit2 /> <span className="btn-text">Edit</span>
                  </button>
                  <button className="btn-delete" onClick={() => onDelete(note.id)} title="Delete">
                    <FiTrash2 /> <span className="btn-text">Delete</span>
                  </button>
                  {note.archived ? (
                    <button className="btn-unarchive" onClick={() => onUnarchive(note.id)} title="Unarchive">
                      <FiUnlock /> <span className="btn-text">Unarchive</span>
                    </button>
                  ) : (
                    <button className="btn-archive" onClick={() => onArchive(note.id)} title="Archive">
                      <FiArchive /> <span className="btn-text">Archive</span>
                    </button>
                  )}
                </div>
              </td>
            </tr>
          ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default NoteList;
