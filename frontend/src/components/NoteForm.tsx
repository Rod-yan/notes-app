import React, { useState } from 'react';
import { NoteRequest, Note } from '../types';

interface NoteFormProps {
  onSave: (note: NoteRequest) => void;
  onCancel: () => void;
  initialNote?: Note;
}

const NoteForm: React.FC<NoteFormProps> = ({ onSave, onCancel, initialNote }) => {
  const [title, setTitle] = useState(initialNote?.title || '');
  const [content, setContent] = useState(initialNote?.content || '');
  const [categoryNames, setCategoryNames] = useState(
    initialNote?.categories.map(c => c.name).join(', ') || ''
  );

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSave({
      title,
      content,
      archived: initialNote?.archived || false,
      categoryNames: categoryNames.split(',').map(s => s.trim()).filter(s => s !== '')
    });
  };

  return (
    <div className="form-container">
      <h3>{initialNote ? 'Edit Note' : 'New Note'}</h3>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Title:</label>
          <input
            type="text"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            required
          />
        </div>
        <div className="form-group">
          <label>Content:</label>
          <textarea
            value={content}
            onChange={(e) => setContent(e.target.value)}
            required
          />
        </div>
        <div className="form-group">
          <label>Categories (comma separated):</label>
          <input
            type="text"
            value={categoryNames}
            onChange={(e) => setCategoryNames(e.target.value)}
          />
        </div>
        <div className="button-group">
          <button type="submit">Save</button>
          <button type="button" className="secondary" onClick={onCancel}>Cancel</button>
        </div>
      </form>
    </div>
  );
};

export default NoteForm;
