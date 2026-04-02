import { useState, useEffect } from 'react';
import { Note, NoteRequest, Category } from './types';
import { noteService, categoryService, authService } from './services/api';
import NoteList from './components/NoteList';
import NoteForm from './components/NoteForm';
import CategoryFilter from './components/CategoryFilter';
import Login from './components/Login';
import NoteViewModal from './components/NoteViewModal';   // <-- ADD THIS
import './App.css';

function NotesApp() {
  const [notes, setNotes] = useState<Note[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [isArchivedView, setIsArchivedView] = useState(false);
  const [selectedCategory, setSelectedCategory] = useState('');
  const [editingNote, setEditingNote] = useState<Note | null>(null);
  const [isFormVisible, setIsFormVisible] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [viewingNote, setViewingNote] = useState<Note | null>(null); // Already present

  useEffect(() => {
    fetchNotes();
    fetchCategories();
  }, [isArchivedView, selectedCategory]);

  const fetchNotes = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await noteService.getNotes(isArchivedView, selectedCategory);
      setNotes(data);
    } catch (err: any) {
      console.error('Error fetching notes:', err);
      setError(err.response?.data?.message || 'Failed to fetch notes. Please try again later.');
    } finally {
      setLoading(false);
    }
  };

  const fetchCategories = async () => {
    try {
      const data = await categoryService.getAllCategories();
      setCategories(data);
    } catch (err: any) {
      console.error('Error fetching categories:', err);
    }
  };

  const handleSaveNote = async (noteRequest: NoteRequest) => {
    setError(null);
    try {
      if (editingNote) {
        await noteService.updateNote(editingNote.id, noteRequest);
      } else {
        await noteService.createNote(noteRequest);
      }
      setIsFormVisible(false);
      setEditingNote(null);
      fetchNotes();
      fetchCategories();
    } catch (err: any) {
      console.error('Error saving note:', err);
      if (err.response?.data?.errors) {
        const validationErrors = Object.values(err.response.data.errors).join(', ');
        setError(`Validation error: ${validationErrors}`);
      } else {
        setError(err.response?.data?.message || 'Failed to save note.');
      }
    }
  };

  const handleDeleteNote = async (id: number) => {
    if (window.confirm('Are you sure you want to delete this note?')) {
      setError(null);
      try {
        await noteService.deleteNote(id);
        fetchNotes();
      } catch (err: any) {
        console.error('Error deleting note:', err);
        setError(err.response?.data?.message || 'Failed to delete note.');
      }
    }
  };

  const handleArchiveNote = async (id: number) => {
    setError(null);
    try {
      await noteService.archiveNote(id);
      fetchNotes();
    } catch (err: any) {
      console.error('Error archiving note:', err);
      setError(err.response?.data?.message || 'Failed to archive note.');
    }
  };

  const handleUnarchiveNote = async (id: number) => {
    setError(null);
    try {
      await noteService.unarchiveNote(id);
      fetchNotes();
    } catch (err: any) {
      console.error('Error unarchiving note:', err);
      setError(err.response?.data?.message || 'Failed to unarchive note.');
    }
  };

  return (
    <div className="container">
      <header className="header">
        <h1>My Notes</h1>
        {!isFormVisible && (
          <div className="button-group">
            <button onClick={() => { setIsFormVisible(true); setEditingNote(null); }}>
              Create Note
            </button>
            <button className="secondary" onClick={() => setIsArchivedView(!isArchivedView)}>
              {isArchivedView ? 'View Active Notes' : 'View Archived Notes'}
            </button>
            <button className="btn-logout" onClick={() => { authService.logout(); window.location.reload(); }}>
              Logout
            </button>
          </div>
        )}
      </header>

      {error && <div className="error-message">{error}</div>}

      {isFormVisible && (
        <NoteForm
          onSave={handleSaveNote}
          onCancel={() => { setIsFormVisible(false); setEditingNote(null); }}
          initialNote={editingNote || undefined}
        />
      )}

      {!isFormVisible && (
        <>
          <CategoryFilter
            categories={categories}
            selectedCategory={selectedCategory}
            onCategoryChange={setSelectedCategory}
          />

          <h2>{isArchivedView ? 'Archived Notes' : 'Active Notes'}</h2>

          {loading ? (
            <div className="loading">Loading notes...</div>
          ) : (
            <div className="table-container">
              <NoteList
                notes={notes}
                onView={setViewingNote}
                onEdit={(note) => { setEditingNote(note); setIsFormVisible(true); }}
                onDelete={handleDeleteNote}
                onArchive={handleArchiveNote}
                onUnarchive={handleUnarchiveNote}
              />
            </div>
          )}
        </>
      )}

      {viewingNote && (
        <NoteViewModal note={viewingNote} onClose={() => setViewingNote(null)} />
      )}
    </div>
  );
}

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean | null>(null);

  useEffect(() => {
    setIsAuthenticated(authService.isAuthenticated());
  }, []);

  if (isAuthenticated === null) {
    return <div className="loading">Loading...</div>;
  }

  if (!isAuthenticated) {
    return <Login onLoginSuccess={() => setIsAuthenticated(true)} />;
  }

  return <NotesApp />;
}

export default App;
